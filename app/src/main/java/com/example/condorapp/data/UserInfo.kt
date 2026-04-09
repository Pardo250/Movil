package com.example.condorapp.data

/**
 * Modelo visual de usuario para la capa UI.
 * Separado del UsuarioDto (capa de datos) para respetar la arquitectura por capas.
 */
data class UserInfo(
    val id: Int,
    val nombre: String,
    val email: String
)
