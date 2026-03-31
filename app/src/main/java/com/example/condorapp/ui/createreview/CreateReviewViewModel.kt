package com.example.condorapp.ui.createreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de creación de reseñas.
 * Publica la reseña en el backend via ReviewRepository.
 */
@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewUiState())
    val uiState: StateFlow<CreateReviewUiState> = _uiState.asStateFlow()

    /** Actualiza la calificación seleccionada por el usuario. */
    fun onRatingChange(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    /** Actualiza el comentario del usuario. */
    fun onCommentChange(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }

    /**
     * Publica la reseña en el backend.
     * @param usuarioId ID del usuario autenticado
     * @param articuloId ID del artículo que se está reseñando
     */
    fun onPublish(usuarioId: Int = 1, articuloId: Int = 1) {
        val state = _uiState.value
        if (state.comment.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
            try {
                reviewRepository.createReview(
                    contenido    = state.comment,
                    calificacion = state.rating,
                    usuarioId    = usuarioId,
                    articuloId   = articuloId
                )
                _uiState.update {
                    it.copy(isLoading = false, isSuccess = true, comment = "", rating = 4)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Error al publicar")
                }
            }
        }
    }
}
