package com.example.condorapp.ui.createreview

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.ReviewRepository
import com.example.condorapp.data.repository.UsuarioRepository
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
    private val application: Application
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
                location?.let {
                    lat = it.latitude
                    lng = it.longitude
                }
            } catch (_: Exception) {
                // Si no hay permisos o no hay ubicación, se crea sin coordenadas
            }

            val result = reviewRepository.createReview(
                contenido      = state.comment,
                calificacion   = state.rating,
                usuarioId      = uid,
                articuloId     = articuloId,
                usuarioNombre  = usuarioNombre,
                lat            = lat,
                lng            = lng
            )

            result.onSuccess {
                _uiState.update {
                    it.copy(isLoading = false, isSuccess = true, comment = "", rating = 4)
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

