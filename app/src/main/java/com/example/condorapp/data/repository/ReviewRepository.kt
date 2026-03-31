package com.example.condorapp.data.repository

import com.example.condorapp.data.Review
import com.example.condorapp.data.datasource.ReviewRemoteDataSource
import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRemoteDataSource
) {
    suspend fun getReviewsByArticulo(articuloId: Int): List<Review> {
        return remoteDataSource.getReviewsByArticulo(articuloId).map { dto ->
            Review(
                id       = dto.id.toString(),
                name     = dto.usuario?.nombre ?: "Usuario desconocido",
                rating   = dto.calificacion,
                comment  = dto.contenido,
                likes    = 0
            )
        }
    }

    suspend fun getReviewsByUsuario(usuarioId: Int): List<Review> {
        return remoteDataSource.getReviewsByUsuario(usuarioId).map { dto ->
            Review(
                id       = dto.id.toString(),
                name     = dto.usuario?.nombre ?: "Usuario desconocido",
                rating   = dto.calificacion,
                comment  = dto.contenido,
                likes    = 0
            )
        }
    }

    suspend fun createReview(
        contenido: String,
        calificacion: Int,
        usuarioId: Int,
        articuloId: Int
    ): Review {
        val dto = remoteDataSource.createReview(
            CreateReviewDto(contenido, calificacion, usuarioId, articuloId)
        )
        return Review(
            id      = dto.id.toString(),
            name    = "Tú",
            rating  = dto.calificacion,
            comment = dto.contenido,
            likes   = 0
        )
    }

    suspend fun updateReview(id: Int, contenido: String?, calificacion: Int?): Review {
        val dto = remoteDataSource.updateReview(id, UpdateReviewDto(contenido, calificacion))
        return Review(
            id      = dto.id.toString(),
            name    = dto.usuario?.nombre ?: "Usuario desconocido",
            rating  = dto.calificacion,
            comment = dto.contenido,
            likes   = 0
        )
    }

    suspend fun deleteReview(id: Int) {
        remoteDataSource.deleteReview(id)
    }
}
