package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.ArticuloDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

class ArticuloRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllArticulos(): List<ArticuloDto> {
        val response = apiService.getAllArticulos()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true) {
                return body.data ?: emptyList()
            }
        }
        throw Exception("Error al obtener artículos: ${response.code()} ${response.message()}")
    }

    suspend fun getArticuloById(id: Int): ArticuloDto {
        val response = apiService.getArticuloById(id)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true && body.data != null) {
                return body.data
            }
        }
        throw Exception("Artículo no encontrado (id=$id): ${response.code()}")
    }
}
