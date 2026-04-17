package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para reviews. Consume ApiService directamente.
 * El ApiResponseUnwrapInterceptor se encarga de extraer el campo "data"
 * del wrapper JSON, por lo que aquí recibimos los DTOs ya deserializados.
 */
class ReviewRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getReviewsByArticulo(articuloId: Int): List<ReviewDto> {
        return apiService.getReviewsByArticulo(articuloId)
    }

    suspend fun getReviewsByUsuario(usuarioId: Int): List<ReviewDto> {
        return apiService.getReviewsByUsuario(usuarioId)
    }

    suspend fun createReview(createReviewDto: CreateReviewDto): ReviewDto {
        return apiService.createReview(createReviewDto)
    }

    suspend fun updateReview(id: Int, updateReviewDto: UpdateReviewDto): ReviewDto {
        return apiService.updateReview(id, updateReviewDto)
    }

    suspend fun deleteReview(id: Int) {
        apiService.deleteReview(id)
    }
}
