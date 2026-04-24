package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.UsuarioDto

/**
 * Interfaz para la fuente de datos de usuarios.
 * Permite intercambiar la implementación (Retrofit ↔ Firestore)
 * sin modificar repositorios ni capas superiores.
 */
interface UsuarioDataSource {
    suspend fun getAllUsuarios(): List<UsuarioDto>
    suspend fun getUsuarioById(id: String): UsuarioDto
    suspend fun saveUsuario(id: String, dto: UsuarioDto)
    suspend fun updateUsuario(id: String, fields: Map<String, Any>)
    suspend fun toggleFollow(followerId: String, followingId: String): Boolean
    suspend fun isFollowing(followerId: String, followingId: String): Boolean
    suspend fun getFollowers(userId: String): List<UsuarioDto>
    suspend fun getFollowing(userId: String): List<UsuarioDto>
    suspend fun getFollowingIds(userId: String): List<String>
}
