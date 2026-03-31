package com.example.condorapp.ui.createreview

/**
 * Estado del UI para la pantalla de creación de reseñas. Contiene la calificación, el comentario,
 * el título de la sección y los estados de la petición a la API.
 */
data class CreateReviewUiState(
        val rating: Int = 4,
        val comment: String = "",
        val title: String = "¿Qué tal estuvo tu aventura?",
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val errorMessage: String? = null
)
