package com.example.condorapp.ui.createreview

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.ReviewRepository
import com.example.condorapp.data.repository.UsuarioRepository
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * ViewModel para la pantalla de creación de reseñas.
 * Publica la reseña en Firestore via ReviewRepository.
 * Usa FirebaseAuth para obtener el UID y UsuarioRepository para el nombre
 * del usuario (desnormalización NoSQL).
 * 
 * Sprint 13.5: Captura automáticamente la ubicación GPS del dispositivo
 * al publicar, para que los reviews aparezcan como marcadores en el mapa.
 */
@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    private val usuarioRepository: UsuarioRepository,
    private val application: Application,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewUiState())
    val uiState: StateFlow<CreateReviewUiState> = _uiState.asStateFlow()

    /** UID del usuario autenticado. */
    private val currentUserId: String
        get() = authRepository.currentUser?.uid ?: ""

    /** Actualiza la calificación seleccionada por el usuario. */
    fun onRatingChange(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    /** Actualiza el comentario del usuario. */
    fun onCommentChange(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }

    /** Actualiza la imagen seleccionada por el usuario. */
    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    /**
     * Publica la reseña en Firestore con ubicación GPS automática.
     * @param articuloId ID del artículo que se está reseñando (pasado desde la navegación)
     */
    fun onPublish(articuloId: String = "") {
        val state = _uiState.value
        if (state.comment.isBlank()) return

        val uid = currentUserId
        if (uid.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

            // Obtener el nombre del usuario para desnormalización
            var usuarioNombre = "Usuario"
            val userResult = usuarioRepository.getUsuarioById(uid)
            userResult.onSuccess { user ->
                usuarioNombre = user.nombre
            }

            // Intentar obtener la ubicación GPS actual (falla silenciosa si no hay permisos)
            var lat: Double? = null
            var lng: Double? = null
            try {
                @SuppressLint("MissingPermission")
                val location = LocationServices
                    .getFusedLocationProviderClient(application)
                    .lastLocation
                    .await()
                
                if (location != null) {
                    lat = location.latitude
                    lng = location.longitude
                } else {
                    // Fallback para testing en emulador (coordenadas por defecto)
                    lat = 4.6097
                    lng = -74.0817
                }
            } catch (_: Exception) {
                // Fallback si no hay permisos
                lat = 4.6097
                lng = -74.0817
            }

            // Subir imagen a Firebase Storage si el usuario seleccionó una
            var imageUrl: String? = null
            val imageUri = _uiState.value.selectedImageUri
            if (imageUri != null) {
                try {
                    // Usar la ruta 'users/uid/...' que cumple con las reglas estándar de seguridad
                    val fileName = "users/$uid/reviews/${System.currentTimeMillis()}.jpg"
                    val ref = storage.reference.child(fileName)
                    ref.putFile(imageUri).await()
                    imageUrl = ref.downloadUrl.await().toString()
                } catch (e: Exception) {
                    android.util.Log.e("CreateReviewVM", "Error al subir imagen a Storage", e)
                    // Si falla la subida de imagen, se publica sin imagen
                }
            }

            val result = reviewRepository.createReview(
                contenido      = state.comment,
                calificacion   = state.rating,
                usuarioId      = uid,
                articuloId     = articuloId,
                usuarioNombre  = usuarioNombre,
                lat            = lat,
                lng            = lng,
                imageUrl       = imageUrl
            )

            result.onSuccess {
                _uiState.update {
                    it.copy(isLoading = false, isSuccess = true, comment = "", rating = 4, selectedImageUri = null)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error al publicar")
                }
            }
        }
    }

    /** Resetea el flag de navegación después de navegar. */
    fun onNavigationHandled() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}

