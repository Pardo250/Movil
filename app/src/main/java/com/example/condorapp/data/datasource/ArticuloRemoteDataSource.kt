package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.ArticuloDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para artículos. Consume ApiService directamente.
 * Sin wrapper Response<>: Retrofit lanza HttpException en errores HTTP,
 * y el DataSource valida el campo success del ApiResponse.
 */
class ArticuloRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllArticulos(): List<ArticuloDto> {
        val apiResponse = apiService.getAllArticulos()
        if (apiResponse.success) {
            return apiResponse.data ?: emptyList()
        }
        throw Exception(apiResponse.message ?: "Error al obtener artículos")
    }

    suspend fun getArticuloById(id: Int): ArticuloDto {
        val apiResponse = apiService.getArticuloById(id)
        if (apiResponse.success && apiResponse.data != null) {
            return apiResponse.data
        }
        throw Exception(apiResponse.message ?: "Artículo no encontrado (id=$id)")
    }
}
