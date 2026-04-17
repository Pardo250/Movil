package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.ArticuloDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para artículos. Consume ApiService directamente.
 * El ApiResponseUnwrapInterceptor se encarga de extraer el campo "data"
 * del wrapper JSON, por lo que aquí recibimos los DTOs ya deserializados.
 */
class ArticuloRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllArticulos(): List<ArticuloDto> {
        return apiService.getAllArticulos()
    }

    suspend fun getArticuloById(id: Int): ArticuloDto {
        return apiService.getArticuloById(id)
    }
}
