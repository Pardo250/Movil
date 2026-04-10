package com.example.condorapp.data.repository

import com.example.condorapp.data.Review
import com.example.condorapp.data.datasource.ReviewRemoteDataSource
import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.example.condorapp.data.dto.toReview
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Repositorio de reviews. Orquesta la comunicación entre el DataSource remoto
 * y la capa visual, mapeando DTOs a modelos y devolviendo Result agnóstico.
 *
 * Estándar:
 * - No usa Response de Retrofit (agnóstico a tecnología).
 * - try/catch con HttpException para extraer código de error del backend.
 * - Mapeo bidireccional: DTO→Review (lectura), campos UI→CreateReviewDto (escritura).
 */
class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRemoteDataSource
) {
    suspend fun getReviewsByArticulo(articuloId: Int): Result<List<Review>> {
        return try {
            val reviews = remoteDataSource.getReviewsByArticulo(articuloId).map { it.toReview() }
            Result.success(reviews)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUsuario(usuarioId: Int): Result<List<Review>> {
        return try {
            val reviews = remoteDataSource.getReviewsByUsuario(usuarioId).map { it.toReview() }
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
        usuarioId: Int,
        articuloId: Int
    ): Result<Review> {
        return try {
            val dto = remoteDataSource.createReview(
                CreateReviewDto(contenido, calificacion, usuarioId, articuloId)
            )
            // Al crear, el usuario actual es "Tú"
            val review = Review(
                id        = dto.id.toString(),
                name      = "Tú",
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

    suspend fun updateReview(id: Int, contenido: String?, calificacion: Int?): Result<Review> {
        return try {
            val dto = remoteDataSource.updateReview(id, UpdateReviewDto(contenido, calificacion))
            Result.success(dto.toReview())
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(id: Int): Result<Unit> {
        return try {
            remoteDataSource.deleteReview(id)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
