package com.example.condorapp.ui.map

import com.example.condorapp.data.Review

/**
 * Estado del UI para la pantalla del mapa (Sprint 13.5).
 * El ViewModel entrega la lista ya filtrada (últimas 24h con ubicación).
 */
data class MapUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val reviews: List<Review> = emptyList(),
    val selectedReview: Review? = null
)
