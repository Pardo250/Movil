package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.UsuarioDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllUsuarios(): List<UsuarioDto> {
        val response = apiService.getAllUsuarios()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true) {
                return body.data ?: emptyList()
            }
        }
        throw Exception("Error al obtener usuarios: ${response.code()} ${response.message()}")
    }

    suspend fun getUsuarioById(id: Int): UsuarioDto {
        val response = apiService.getUsuarioById(id)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true && body.data != null) {
                return body.data
            }
        }
        throw Exception("Usuario no encontrado (id=$id): ${response.code()}")
    }
}
