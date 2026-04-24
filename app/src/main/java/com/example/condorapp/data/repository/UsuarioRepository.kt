package com.example.condorapp.data.repository

import com.example.condorapp.data.UserInfo
import com.example.condorapp.data.datasource.UsuarioDataSource
import com.example.condorapp.data.dto.UsuarioDto
import com.example.condorapp.data.dto.toUserInfo
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Repositorio de usuarios. Mapea UsuarioDto → UserInfo para respetar
 * la separación DTO↔Modelo visual. Devuelve Result agnóstico.
 *
 * Depende de la interfaz UsuarioDataSource, no de una implementación concreta.
 * Esto permite cambiar Firestore ↔ Retrofit sin tocar este archivo.
 */
class UsuarioRepository @Inject constructor(
    private val dataSource: UsuarioDataSource
) {
    suspend fun getAllUsuarios(): Result<List<UserInfo>> {
        return try {
            val usuarios = dataSource.getAllUsuarios().map { it.toUserInfo() }
            Result.success(usuarios)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsuarioById(id: String): Result<UserInfo> {
        return try {
            val usuario = dataSource.getUsuarioById(id).toUserInfo()
            Result.success(usuario)
        } catch (e: HttpException) {
            Result.failure(Exception("Error ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Guarda un nuevo usuario (usado en el registro).
     * @param uid UID de FirebaseAuth usado como ID del documento
     */
    suspend fun saveUsuario(uid: String, nombre: String, email: String, username: String): Result<Unit> {
        return try {
            val dto = UsuarioDto(
                id = uid,
                nombre = nombre,
                email = email,
                username = username
            )
            dataSource.saveUsuario(uid, dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza campos del perfil del usuario.
     * Solo modifica los campos enviados (merge).
     */
    suspend fun updateUsuario(uid: String, fields: Map<String, Any>): Result<Unit> {
        return try {
            dataSource.updateUsuario(uid, fields)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFollow(followerId: String, followingId: String): Result<Boolean> {
        return try {
            val isNowFollowing = dataSource.toggleFollow(followerId, followingId)
            Result.success(isNowFollowing)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isFollowing(followerId: String, followingId: String): Result<Boolean> {
        return try {
            val following = dataSource.isFollowing(followerId, followingId)
            Result.success(following)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowers(userId: String): Result<List<UserInfo>> {
        return try {
            val followers = dataSource.getFollowers(userId).map { it.toUserInfo() }
            Result.success(followers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowing(userId: String): Result<List<UserInfo>> {
        return try {
            val following = dataSource.getFollowing(userId).map { it.toUserInfo() }
            Result.success(following)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowingIds(userId: String): Result<List<String>> {
        return try {
            val ids = dataSource.getFollowingIds(userId)
            Result.success(ids)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
