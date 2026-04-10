package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.UsuarioDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para usuarios. Consume ApiService directamente.
 * Sin wrapper Response<>: Retrofit lanza HttpException en errores HTTP,
 * y el DataSource valida el campo success del ApiResponse.
 */
class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllUsuarios(): List<UsuarioDto> {
        val apiResponse = apiService.getAllUsuarios()
        if (apiResponse.success) {
            return apiResponse.data ?: emptyList()
        }
        throw Exception(apiResponse.message ?: "Error al obtener usuarios")
    }

    suspend fun getUsuarioById(id: Int): UsuarioDto {
        val apiResponse = apiService.getUsuarioById(id)
        if (apiResponse.success && apiResponse.data != null) {
            return apiResponse.data
        }
        throw Exception(apiResponse.message ?: "Usuario no encontrado (id=$id)")
    }
}
