package com.example.condorapp.data.repository

import com.example.condorapp.data.UserInfo
import com.example.condorapp.data.datasource.UsuarioRemoteDataSource
import com.example.condorapp.data.dto.toUserInfo
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Repositorio de usuarios. Mapea UsuarioDto → UserInfo para respetar
 * la separación DTO↔Modelo visual. Devuelve Result agnóstico.
 */
class UsuarioRepository @Inject constructor(
    private val remoteDataSource: UsuarioRemoteDataSource
) {
    suspend fun getAllUsuarios(): Result<List<UserInfo>> {
        return try {
            val usuarios = remoteDataSource.getAllUsuarios().map { it.toUserInfo() }
            Result.success(usuarios)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsuarioById(id: Int): Result<UserInfo> {
        return try {
            val usuario = remoteDataSource.getUsuarioById(id).toUserInfo()
            Result.success(usuario)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
