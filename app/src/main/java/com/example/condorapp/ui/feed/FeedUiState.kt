package com.example.condorapp.ui.feed

import com.example.condorapp.data.Articulo

/**
 * Estado del UI para la pantalla de exploración (Feed). Contiene los artículos
 * del backend como recomendaciones y estado de carga/error.
 */
data class FeedUiState(
        val selectedCategoryIndex: Int = 0,
        val articulos: List<Articulo> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
)
