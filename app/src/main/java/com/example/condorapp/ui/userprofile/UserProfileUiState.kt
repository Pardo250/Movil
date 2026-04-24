package com.example.condorapp.ui.userprofile

import com.example.condorapp.data.Review
import com.example.condorapp.data.UserInfo

/**
 * Estado del UI para la pantalla de perfil de otro usuario.
 * Contiene datos del usuario del backend y sus reviews.
 */
data class UserProfileUiState(
    val user: UserInfo? = null,
    val reviews: List<Review> = emptyList(),
    val isFollowing: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
