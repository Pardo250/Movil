package com.example.condorapp.ui.detail

import com.example.condorapp.data.Review

/**
 * Estado del UI para la pantalla de detalle de un artículo. Contiene información del artículo
 * del backend, las reseñas de la comunidad y estado de carga/error para la API REST.
 */
data class DetailUiState(
        val articuloId: Int = 0,
        val title: String = "",
        val location: String = "",
        val description: String = "",
        val imageUrl: String = "",
        val reviews: List<Review> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isEditingReview: Boolean = false,
        val editReviewId: String? = null,
        val editComment: String = "",
        val editRating: Int = 4
)
