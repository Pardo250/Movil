package com.example.condorapp.ui.feed

import com.example.condorapp.data.FeedPlace

/**
 * Estado del UI para la pantalla de exploración (Feed). Contiene la categoría seleccionada,
 * los lugares disponibles y estado de carga/error para la API REST.
 */
data class FeedUiState(
        val selectedCategoryIndex: Int = 0,
        val places: List<FeedPlace> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
)
