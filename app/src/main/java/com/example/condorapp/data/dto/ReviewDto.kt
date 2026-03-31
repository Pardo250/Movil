package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("id")           val id: Int,
    @SerializedName("contenido")    val contenido: String,
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("usuarioId")    val usuarioId: Int,
    @SerializedName("articuloId")   val articuloId: Int,
    @SerializedName("createdAt")    val createdAt: String,
    @SerializedName("updatedAt")    val updatedAt: String,
    // Incluidos cuando se consulta por artículo o usuario
    @SerializedName("Usuario")      val usuario: UsuarioDto? = null,
    @SerializedName("Articulo")     val articulo: ArticuloDto? = null
)
