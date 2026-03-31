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
     * Carga los datos de una reseña específica (Por ahora la API no tiene un endpoint para
     * buscar una Review aislada, por lo que usamos las de un artículo predeterminado y extraemos la principal)
     */
    fun loadReview(reviewId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Como el backend Express no tiene GET /reviews/:id
                // Simulamos trayendo las reviews de un artículo por defecto para mantener el UI funcionando
                val reviews = reviewRepository.getReviewsByArticulo(1)
                
                val mainReview = reviews.find { it.id == reviewId } ?: reviews.firstOrNull()
                // El backend tampoco soporta respuestas/comentarios anidados, las dejamos vacías por ahora.
                val comments = emptyList<com.example.condorapp.data.Review>()

                _uiState.update { 
                    it.copy(review = mainReview, comments = comments, isLoading = false) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = e.message ?: "Error desconocido")
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
