package com.example.condorapp.ui.review

import androidx.lifecycle.ViewModel
import com.example.condorapp.data.local.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de detalle de una reseña. Carga la reseña principal y sus comentarios
 * desde el repositorio.
 */
class ReviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewScreenUiState())
    val uiState: StateFlow<ReviewScreenUiState> = _uiState.asStateFlow()

    /** Carga los datos de una reseña específica por su ID. */
    fun loadReview(reviewId: String) {
        val reviews = ReviewRepository.getReviews()
        val mainReview = reviews.firstOrNull()
        val comments = reviews.drop(1)

        _uiState.update { it.copy(review = mainReview, comments = comments) }
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
