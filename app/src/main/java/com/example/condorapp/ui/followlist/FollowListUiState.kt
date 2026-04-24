package com.example.condorapp.ui.followlist

import com.example.condorapp.data.UserInfo

/**
 * Estado del UI para la pantalla de lista de Seguidores/Seguidos.
 * Contiene las listas de usuarios, el tab seleccionado y el estado de carga.
 */
data class FollowListUiState(
    val followers: List<UserInfo> = emptyList(),
    val following: List<UserInfo> = emptyList(),
    val selectedTab: Int = 0, // 0 = Seguidores, 1 = Siguiendo
    val myFollowingIds: Set<String> = emptySet(), // Para saber si el usuario actual sigue a alguien en la lista
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
