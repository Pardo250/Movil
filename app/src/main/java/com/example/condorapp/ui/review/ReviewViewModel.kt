package com.example.condorapp.ui.review

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
 * ViewModel para la pantalla de detalle de una reseña.
 */
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewScreenUiState())
    val uiState: StateFlow<ReviewScreenUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos de una reseña específica.
     * Busca en las reviews de todos los artículos (Firestore no tiene endpoint por review ID).
     */
    fun loadReview(reviewId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = reviewRepository.getReviewById(reviewId)

            result.onSuccess { review ->
                if (review != null) {
                    _uiState.update {
                        it.copy(review = review, comments = emptyList(), isLoading = false)
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Reseña no encontrada")
                    }
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error desconocido")
                }
            }
        }
    }

    /** Actualiza el campo de comentario del usuario. */
    fun onUserCommentChange(comment: String) {
        _uiState.update { it.copy(userComment = comment) }
    }

    /** Publica el comentario y limpia el campo de texto. */
    fun onPostComment() {
        if (_uiState.value.userComment.isNotBlank()) {
            _uiState.update { it.copy(userComment = "") }
        }
    }
}
