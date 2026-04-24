package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import kotlinx.coroutines.flow.Flow

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
    suspend fun toggleLike(reviewId: String, userId: String): Boolean
    suspend fun isLikedByUser(reviewId: String, userId: String): Boolean
    fun listenReviewsByArticulo(articuloId: String): Flow<List<ReviewDto>>
    suspend fun getAllReviews(): List<ReviewDto>
}
