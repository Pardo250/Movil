package com.example.condorapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.Review
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.ReviewRepository
import com.example.condorapp.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de perfil del usuario. Carga los datos del perfil
 * desde Firestore, las reviews del usuario, y permite editar/eliminar sus propias reviews.
 *
 * Usa FirebaseAuth.currentUser.uid como ID del usuario actual.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val usuarioRepository: UsuarioRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    /** UID del usuario actualmente autenticado. */
    private val currentUserId: String
        get() = authRepository.currentUser?.uid ?: ""

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
        loadMyReviews()
    }

    /** Carga los datos del perfil del usuario desde Firestore. */
    private fun loadProfile() {
        val uid = currentUserId
        if (uid.isEmpty()) return

        viewModelScope.launch {
            val result = usuarioRepository.getUsuarioById(uid)
            result.onSuccess { user ->
                val photoUrl = authRepository.currentUser?.photoUrl?.toString()
                _uiState.update {
                    it.copy(
                        name     = user.nombre,
                        username = user.username,
                        imageUrl = photoUrl ?: user.avatarUrl.ifEmpty { null },
                        followersCount = user.followersCount,
                        followingCount = user.followingCount
                    )
                }
            }.onFailure { error ->
                // Fallback: usar datos de FirebaseAuth
                val firebaseUser = authRepository.currentUser
                _uiState.update {
                    it.copy(
                        name     = firebaseUser?.displayName ?: "Usuario",
                        username = "@${firebaseUser?.email?.substringBefore("@") ?: "user"}",
                        imageUrl = firebaseUser?.photoUrl?.toString()
                    )
                }
            }
        }
    }

    /** Carga las reviews que ha realizado el usuario actual desde Firestore. */
    fun loadMyReviews() {
        val uid = currentUserId
        if (uid.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = reviewRepository.getReviewsByUsuario(uid)

            result.onSuccess { reviews ->
                _uiState.update { it.copy(reviews = reviews, isLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar reviews")
                }
            }
        }
    }

    // ─── Edición de reviews propias ─────────────────────────────────────────

    /** Inicia el modo edición para una review propia. */
    fun startEditReview(review: Review) {
        _uiState.update {
            it.copy(
                isEditingReview = true,
                editReviewId = review.id,
                editComment = review.comment,
                editRating = review.rating
            )
        }
    }

    /** Cancela la edición. */
    fun cancelEditReview() {
        _uiState.update {
            it.copy(isEditingReview = false, editReviewId = null, editComment = "", editRating = 4)
        }
    }

    /** Actualiza el campo de comentario en modo edición. */
    fun onEditCommentChange(comment: String) {
        _uiState.update { it.copy(editComment = comment) }
    }

    /** Actualiza el rating en modo edición. */
    fun onEditRatingChange(rating: Int) {
        _uiState.update { it.copy(editRating = rating) }
    }

    /** Confirma la edición y envía a Firestore. */
    fun confirmEditReview() {
        val state = _uiState.value
        val reviewId = state.editReviewId ?: return

        viewModelScope.launch {
            val result = reviewRepository.updateReview(
                id = reviewId,
                contenido = state.editComment,
                calificacion = state.editRating
            )

            result.onSuccess { updatedReview ->
                _uiState.update { currentState ->
                    val updatedReviews = currentState.reviews.map { r ->
                        if (r.id == state.editReviewId) updatedReview else r
                    }
                    currentState.copy(
                        reviews = updatedReviews,
                        isEditingReview = false,
                        editReviewId = null,
                        editComment = "",
                        editRating = 4
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "Error al actualizar reseña")
                }
            }
        }
    }

    /** Elimina una review propia. */
    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            val result = reviewRepository.deleteReview(reviewId)

            result.onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(
                        reviews = currentState.reviews.filter { it.id != reviewId }
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "Error al eliminar reseña")
                }
            }
        }
    }
}
