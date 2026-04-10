package com.example.condorapp.data

/**
 * Modelo visual de una reseña para la capa UI.
 * Incluye el usuarioId para navegación y articuloNombre para contexto.
 */
data class Review(
    val id: String,
    val name: String,
    val rating: Int,
    val comment: String,
    val likes: Int,
    val usuarioId: Int = 0,
    val articuloNombre: String = ""
)
