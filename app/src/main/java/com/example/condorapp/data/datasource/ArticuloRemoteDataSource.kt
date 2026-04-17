package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.ArticuloDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para artículos vía Retrofit/API REST.
 * Implementa la interfaz ArticuloDataSource para permitir intercambio con Firestore.
 * El ApiResponseUnwrapInterceptor se encarga de extraer el campo "data" del wrapper JSON.
 */
class ArticuloRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : ArticuloDataSource {

    override suspend fun getAllArticulos(): List<ArticuloDto> {
        return apiService.getAllArticulos()
    }

    override suspend fun getArticuloById(id: String): ArticuloDto {
        return apiService.getArticuloById(id.toInt())
    }
}
