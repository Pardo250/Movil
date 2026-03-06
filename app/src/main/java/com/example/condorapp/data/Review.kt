package com.example.condorapp.data

// ✅ Se añade un ID para identificar cada reseña de forma única.
data class Review(
    val id: String,
    val name: String,
    val rating: Int,
    val comment: String,
    val likes: Int
)
