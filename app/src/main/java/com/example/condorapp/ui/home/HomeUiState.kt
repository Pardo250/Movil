package com.example.condorapp.ui.home

import com.example.condorapp.data.Articulo
import com.example.condorapp.data.Review

/**
 * Estado del UI para la pantalla Home. Contiene la lista de artículos del backend,
 * el artículo seleccionado y estados de carga/error.
 */
data class HomeUiState(
    val articulos: List<Articulo> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val selectedIndex: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showFollowingOnly: Boolean = false
)
