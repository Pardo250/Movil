package com.example.condorapp.ui.review

import com.example.condorapp.data.Review

/**
 * Estado del UI para la pantalla de detalle de una reseña. Contiene la reseña principal, sus
 * comentarios, el campo de input del usuario y estados de la llamada a la API.
 */
data class ReviewScreenUiState(
        val review: Review? = null,
        val comments: List<Review> = emptyList(),
        val userComment: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
)
