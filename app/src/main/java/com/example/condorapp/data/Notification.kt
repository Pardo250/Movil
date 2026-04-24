package com.example.condorapp.data

/**
 * Modelo de datos que representa una notificación en la app.
 * Compatible con Firestore: todos los campos tienen valores por defecto.
 */
data class Notification(
    val id: String = "",
    val userName: String = "",
    val action: String = "",
    val time: String = "",
    val avatarUrl: String = "",
    val type: String = "like",       // "like" | "follow"
    val createdAt: Long = 0L
)
