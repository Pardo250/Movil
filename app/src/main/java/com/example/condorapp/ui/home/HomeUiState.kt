package com.example.condorapp.ui.home

import com.example.condorapp.data.Articulo

/**
 * Estado del UI para la pantalla Home. Contiene la lista de artículos del backend,
 * el artículo seleccionado y estados de carga/error.
 */
data class HomeUiState(
    val articulos: List<Articulo> = emptyList(),
    val selectedIndex: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
