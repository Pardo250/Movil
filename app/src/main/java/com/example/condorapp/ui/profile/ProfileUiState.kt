package com.example.condorapp.ui.profile

/**
 * Estado del UI para la pantalla de perfil del usuario. Contiene datos del perfil y las fotos del
 * usuario.
 */
data class ProfileUiState(
    val name: String = "",
    val username: String = "",
    val imageUrl: String? = null,
    val photos: List<String> = emptyList()
)
