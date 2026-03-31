package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

data class UsuarioDto(
    @SerializedName("id")        val id: Int,
    @SerializedName("nombre")    val nombre: String,
    @SerializedName("email")     val email: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)
