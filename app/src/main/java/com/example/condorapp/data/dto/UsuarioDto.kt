package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para usuarios. Compatible con Firestore (constructor vacío, IDs String)
 * y con Retrofit/Gson (@SerializedName). Los campos adicionales de perfil
 * (bio, avatarUrl, username) se usan en Firestore; Retrofit los ignora.
 */
data class UsuarioDto(
    @SerializedName("id")        val id: String = "",
    @SerializedName("nombre")    val nombre: String = "",
    @SerializedName("email")     val email: String = "",
    @SerializedName("username")  val username: String = "",
    @SerializedName("bio")       val bio: String = "",
    @SerializedName("avatarUrl") val avatarUrl: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = ""
)
