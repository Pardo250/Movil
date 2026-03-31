package com.example.condorapp.data.repository

import com.example.condorapp.data.Articulo
import com.example.condorapp.data.datasource.ArticuloRemoteDataSource
import javax.inject.Inject

class ArticuloRepository @Inject constructor(
    private val remoteDataSource: ArticuloRemoteDataSource
) {
    suspend fun getAllArticulos(): List<Articulo> {
        return remoteDataSource.getAllArticulos().map { dto ->
            Articulo(
                id          = dto.id,
                titulo      = dto.titulo,
                descripcion = dto.descripcion ?: "",
                tipo        = dto.tipo
            )
        }
    }

    suspend fun getArticuloById(id: Int): Articulo {
        val dto = remoteDataSource.getArticuloById(id)
        return Articulo(
            id          = dto.id,
            titulo      = dto.titulo,
            descripcion = dto.descripcion ?: "",
            tipo        = dto.tipo
        )
    }
}
