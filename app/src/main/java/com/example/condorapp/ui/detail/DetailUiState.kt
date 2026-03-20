package com.example.condorapp.ui.detail

import com.example.condorapp.R
import com.example.condorapp.data.Review

/**
 * Estado del UI para la pantalla de detalle de un destino. Contiene información del lugar y las
 * reseñas de la comunidad.
 */
data class DetailUiState(
        val title: String = "",
        val location: String = "",
        val description: String =
                "Descubre este increíble destino colombiano, lleno de historia, cultura y paisajes inolvidables que te conectarán con la naturaleza.",
        val imageUrl: String = "https://example.com/valle_del_cocora.jpg",
        val reviews: List<Review> = emptyList()
)
