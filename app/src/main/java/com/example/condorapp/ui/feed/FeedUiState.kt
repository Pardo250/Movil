package com.example.condorapp.ui.feed

import com.example.condorapp.data.FeedPlace

/**
 * Estado del UI para la pantalla de exploración (Feed). Contiene la categoría seleccionada y los
 * lugares disponibles.
 */
data class FeedUiState(
        val selectedCategoryIndex: Int = 0,
        val places: List<FeedPlace> = emptyList()
)
