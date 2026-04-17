package com.example.condorapp.data.repository

import com.example.condorapp.data.Review
import com.example.condorapp.data.datasource.ReviewDataSource
import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.example.condorapp.data.dto.toReview
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Repositorio de reviews. Orquesta la comunicación entre el DataSource
 * y la capa visual, mapeando DTOs a modelos y devolviendo Result agnóstico.
 *
 * Depende de la interfaz ReviewDataSource, no de una implementación concreta.
 * Esto permite cambiar Firestore ↔ Retrofit sin tocar este archivo.
 *
 * Estándar:
 * - No usa Response de Retrofit (agnóstico a tecnología).
 * - try/catch con HttpException para extraer código de error del backend.
 * - Mapeo bidireccional: DTO→Review (lectura), campos UI→CreateReviewDto (escritura).
 */
class ReviewRepository @Inject constructor(
    private val dataSource: ReviewDataSource
) {
    suspend fun getReviewsByArticulo(articuloId: String): Result<List<Review>> {
        return try {
            val reviews = dataSource.getReviewsByArticulo(articuloId).map { it.toReview() }
            Result.success(reviews)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUsuario(usuarioId: String): Result<List<Review>> {
        return try {
            val reviews = dataSource.getReviewsByUsuario(usuarioId).map { it.toReview() }
            Result.success(reviews)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(
        contenido: String,
        calificacion: Int,
        usuarioId: String,
        articuloId: String,
        usuarioNombre: String = ""
    ): Result<Review> {
        return try {
            val dto = dataSource.createReview(
                CreateReviewDto(contenido, calificacion, usuarioId, articuloId, usuarioNombre)
            )
            val review = Review(
                id        = dto.id,
                name      = dto.usuarioNombre.ifEmpty { "Tú" },
                rating    = dto.calificacion,
                comment   = dto.contenido,
                likes     = 0,
                usuarioId = usuarioId
            )
            Result.success(review)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(id: String, contenido: String?, calificacion: Int?): Result<Review> {
        return try {
            val dto = dataSource.updateReview(id, UpdateReviewDto(contenido, calificacion))
            Result.success(dto.toReview())
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(id: String): Result<Unit> {
        return try {
            dataSource.deleteReview(id)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
