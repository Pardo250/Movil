package com.example.condorapp.data

/**
 * Modelo visual de usuario para la capa UI.
 * Separado del UsuarioDto (capa de datos) para respetar la arquitectura por capas.
 * Incluye campos de perfil (bio, avatarUrl, username) para la pantalla de perfil.
 */
data class UserInfo(
    val id: String,
    val nombre: String,
    val email: String,
    val username: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0
)
