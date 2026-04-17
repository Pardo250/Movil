package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para reviews vía Retrofit/API REST.
 * Implementa la interfaz ReviewDataSource para permitir intercambio con Firestore.
 * El ApiResponseUnwrapInterceptor se encarga de extraer el campo "data" del wrapper JSON.
 */
class ReviewRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : ReviewDataSource {

    override suspend fun getReviewsByArticulo(articuloId: String): List<ReviewDto> {
        return apiService.getReviewsByArticulo(articuloId.toInt())
    }

    override suspend fun getReviewsByUsuario(usuarioId: String): List<ReviewDto> {
        return apiService.getReviewsByUsuario(usuarioId.toInt())
    }

    override suspend fun createReview(dto: CreateReviewDto): ReviewDto {
        return apiService.createReview(dto)
    }

    override suspend fun updateReview(id: String, dto: UpdateReviewDto): ReviewDto {
        return apiService.updateReview(id.toInt(), dto)
    }

    override suspend fun deleteReview(id: String) {
        apiService.deleteReview(id.toInt())
    }
}
