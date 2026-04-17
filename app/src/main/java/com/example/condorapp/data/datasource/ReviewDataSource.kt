package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto

/**
 * Interfaz para la fuente de datos de reviews.
 * Permite intercambiar la implementación (Retrofit ↔ Firestore)
 * sin modificar repositorios ni capas superiores.
 */
interface ReviewDataSource {
    suspend fun getReviewsByArticulo(articuloId: String): List<ReviewDto>
    suspend fun getReviewsByUsuario(usuarioId: String): List<ReviewDto>
    suspend fun createReview(dto: CreateReviewDto): ReviewDto
    suspend fun updateReview(id: String, dto: UpdateReviewDto): ReviewDto
    suspend fun deleteReview(id: String)
}
