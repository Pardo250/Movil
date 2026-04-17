package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.UsuarioDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para usuarios. Consume ApiService directamente.
 * El ApiResponseUnwrapInterceptor se encarga de extraer el campo "data"
 * del wrapper JSON, por lo que aquí recibimos los DTOs ya deserializados.
 */
class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllUsuarios(): List<UsuarioDto> {
        return apiService.getAllUsuarios()
    }

    suspend fun getUsuarioById(id: Int): UsuarioDto {
        return apiService.getUsuarioById(id)
    }
}
