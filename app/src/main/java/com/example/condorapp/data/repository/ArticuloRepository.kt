package com.example.condorapp.data.repository

import com.example.condorapp.data.Articulo
import com.example.condorapp.data.datasource.ArticuloDataSource
import com.example.condorapp.data.dto.toArticulo
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Repositorio de artículos. Orquesta la comunicación entre el DataSource
 * y la capa visual, mapeando DTOs a modelos y devolviendo Result agnóstico.
 *
 * Depende de la interfaz ArticuloDataSource, no de una implementación concreta.
 * Esto permite cambiar Firestore ↔ Retrofit sin tocar este archivo.
 */
class ArticuloRepository @Inject constructor(
    private val dataSource: ArticuloDataSource
) {
    suspend fun getAllArticulos(): Result<List<Articulo>> {
        return try {
            val articulos = dataSource.getAllArticulos().map { it.toArticulo() }
            Result.success(articulos)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArticuloById(id: String): Result<Articulo> {
        return try {
            val articulo = dataSource.getArticuloById(id).toArticulo()
            Result.success(articulo)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
