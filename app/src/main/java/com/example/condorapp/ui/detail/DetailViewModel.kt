package com.example.condorapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.Review
import com.example.condorapp.data.repository.ArticuloRepository
import com.example.condorapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de un artículo.
 * Carga el artículo y sus reseñas desde el backend.
 *
 * CURRENT_USER_ID = 1 → ID del usuario "quemado" según la guía del Sprint.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    companion object {
        const val CURRENT_USER_ID = 1
    }

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos de un artículo y sus reseñas desde el backend.
     * @param postId ID del artículo (String que se parsea a Int)
     */
    fun loadPostDetail(postId: String) {
        val articuloId = postId.toIntOrNull() ?: 1

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, articuloId = articuloId) }

            // Cargar artículo del backend
            val articuloResult = articuloRepository.getArticuloById(articuloId)
            articuloResult.onSuccess { articulo ->
                _uiState.update {
                    it.copy(
                        title       = articulo.titulo,
                        location    = articulo.tipo,
                        description = articulo.descripcion.ifBlank {
                            "Descubre este increíble contenido, lleno de detalles y experiencias que te encantarán."
                        }
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(title = "Artículo #$articuloId", location = "Sin conexión")
                }
            }

            // Cargar reviews del artículo
            val reviewsResult = reviewRepository.getReviewsByArticulo(articuloId)
            reviewsResult.onSuccess { reviews ->
                _uiState.update { it.copy(reviews = reviews, isLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar reseñas")
                }
            }
        }
    }

    /** Incrementa los likes de una reseña específica (solo local). */
    fun onLikeReview(review: Review) {
        _uiState.update { currentState ->
            val updatedReviews = currentState.reviews.map {
                if (it == review) it.copy(likes = it.likes + 1) else it
            }
            currentState.copy(reviews = updatedReviews)
        }
    }

    /**
     * Elimina una reseña. Aplica filtro optimista de la lista local.
     */
    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            val id = reviewId.toIntOrNull() ?: return@launch
            val result = reviewRepository.deleteReview(id)

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

    // ─── Edición inline de reviews ─────────────────────────────────────────

    /** Inicia el modo edición para una review específica. */
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

    /** Confirma la edición y envía al backend. */
    fun confirmEditReview() {
        val state = _uiState.value
        val reviewId = state.editReviewId?.toIntOrNull() ?: return

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
}
