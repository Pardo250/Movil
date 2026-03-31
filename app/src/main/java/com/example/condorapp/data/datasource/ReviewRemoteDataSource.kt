package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

class ReviewRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getReviewsByArticulo(articuloId: Int): List<ReviewDto> {
        val response = apiService.getReviewsByArticulo(articuloId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true) {
                return body.data ?: emptyList()
            }
        }
        throw Exception("Error al obtener reviews del artículo $articuloId: ${response.code()}")
    }

    suspend fun getReviewsByUsuario(usuarioId: Int): List<ReviewDto> {
        val response = apiService.getReviewsByUsuario(usuarioId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true) {
                return body.data ?: emptyList()
            }
        }
        throw Exception("Error al obtener reviews del usuario $usuarioId: ${response.code()}")
    }

    suspend fun createReview(createReviewDto: CreateReviewDto): ReviewDto {
        val response = apiService.createReview(createReviewDto)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true && body.data != null) {
                return body.data
            }
        }
        throw Exception("Error al crear review: ${response.code()} ${response.message()}")
    }

    suspend fun updateReview(id: Int, updateReviewDto: UpdateReviewDto): ReviewDto {
        val response = apiService.updateReview(id, updateReviewDto)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true && body.data != null) {
                return body.data
            }
        }
        throw Exception("Error al actualizar review $id: ${response.code()}")
    }

    suspend fun deleteReview(id: Int) {
        val response = apiService.deleteReview(id)
        if (!response.isSuccessful || response.body()?.success != true) {
            throw Exception("Error al eliminar review $id: ${response.code()}")
        }
    }
}
