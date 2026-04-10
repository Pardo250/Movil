package com.example.condorapp.ui.profile

import com.example.condorapp.data.Review

/**
 * Estado del UI para la pantalla de perfil del usuario. Contiene datos del perfil,
 * las reviews del usuario desde el backend, y estados de carga/error/edición.
 */
data class ProfileUiState(
    val name: String = "",
    val username: String = "",
    val imageUrl: String? = null,
    val photos: List<String> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // Campos de edición inline
    val isEditingReview: Boolean = false,
    val editReviewId: String? = null,
    val editComment: String = "",
    val editRating: Int = 4
)
