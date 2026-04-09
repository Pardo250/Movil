package com.example.condorapp.data.repository

import com.example.condorapp.data.Articulo
import com.example.condorapp.data.datasource.ArticuloRemoteDataSource
import com.example.condorapp.data.dto.toArticulo
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Repositorio de artículos. Orquesta la comunicación entre el DataSource remoto
 * y la capa visual, mapeando DTOs a modelos y devolviendo Result agnóstico.
 */
class ArticuloRepository @Inject constructor(
    private val remoteDataSource: ArticuloRemoteDataSource
) {
    suspend fun getAllArticulos(): Result<List<Articulo>> {
        return try {
            val articulos = remoteDataSource.getAllArticulos().map { it.toArticulo() }
            Result.success(articulos)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArticuloById(id: Int): Result<Articulo> {
        return try {
            val articulo = remoteDataSource.getArticuloById(id).toArticulo()
            Result.success(articulo)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
