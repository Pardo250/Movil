package com.example.condorapp.ui.createreview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de creación de reseñas. Gestiona la calificación, el comentario y la
 * acción de publicar.
 */
class CreateReviewViewModel : ViewModel() {

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

    /** Publica la reseña e imprime en consola el resultado. */
    fun onPublish() {
        val state = _uiState.value
        println("Reseña publicada: Rating=${state.rating}, Comentario=${state.comment}")
    }
}
