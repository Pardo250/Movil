package com.example.condorapp.ui.detail

import com.example.condorapp.data.Review

/**
 * Estado del UI para la pantalla de detalle de un artículo. Contiene información del artículo
 * del backend, las reseñas de la comunidad y estado de carga/error.
 *
 * Nota: Editar/eliminar reviews se hace desde la pantalla de perfil, no desde el detalle.
 */
data class DetailUiState(
        val articuloId: String = "",
        val title: String = "",
        val location: String = "",
        val description: String = "",
        val imageUrl: String = "",
        val reviews: List<Review> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
)
