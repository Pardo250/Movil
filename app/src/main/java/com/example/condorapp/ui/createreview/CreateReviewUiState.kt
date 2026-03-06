package com.example.condorapp.ui.createreview

/**
 * Estado del UI para la pantalla de creación de reseñas. Contiene la calificación, el comentario y
 * el título de la sección.
 */
data class CreateReviewUiState(
        val rating: Int = 4,
        val comment: String = "",
        val title: String = "¿Qué tal estuvo tu aventura?"
)
