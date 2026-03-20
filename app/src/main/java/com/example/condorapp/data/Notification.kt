package com.example.condorapp.data

/** Modelo de datos que representa una notificación en la app. */
data class Notification(
        val id: Int,
        val userName: String,
        val action: String,
        val time: String,
        val avatarUrl: String
)
