package com.example.condorapp.ui.map

import com.example.condorapp.data.Review

data class MapUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val filteredReviews: List<Review> = emptyList()
)
