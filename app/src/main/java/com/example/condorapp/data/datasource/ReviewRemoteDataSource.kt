package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para reviews. Consume ApiService directamente.
 * Sin wrapper Response<>: Retrofit lanza HttpException en errores HTTP,
 * y el DataSource valida el campo success del ApiResponse.
 */
class ReviewRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getReviewsByArticulo(articuloId: Int): List<ReviewDto> {
        val apiResponse = apiService.getReviewsByArticulo(articuloId)
        if (apiResponse.success) {
            return apiResponse.data ?: emptyList()
        }
        throw Exception(apiResponse.message ?: "Error al obtener reviews del artículo $articuloId")
    }

    suspend fun getReviewsByUsuario(usuarioId: Int): List<ReviewDto> {
        val apiResponse = apiService.getReviewsByUsuario(usuarioId)
        if (apiResponse.success) {
            return apiResponse.data ?: emptyList()
        }
        throw Exception(apiResponse.message ?: "Error al obtener reviews del usuario $usuarioId")
    }

    suspend fun createReview(createReviewDto: CreateReviewDto): ReviewDto {
        val apiResponse = apiService.createReview(createReviewDto)
        if (apiResponse.success && apiResponse.data != null) {
            return apiResponse.data
        }
        throw Exception(apiResponse.message ?: "Error al crear review")
    }

    suspend fun updateReview(id: Int, updateReviewDto: UpdateReviewDto): ReviewDto {
        val apiResponse = apiService.updateReview(id, updateReviewDto)
        if (apiResponse.success && apiResponse.data != null) {
            return apiResponse.data
        }
        throw Exception(apiResponse.message ?: "Error al actualizar review $id")
    }

    suspend fun deleteReview(id: Int) {
        val apiResponse = apiService.deleteReview(id)
        if (!apiResponse.success) {
            throw Exception(apiResponse.message ?: "Error al eliminar review $id")
        }
    }
}
