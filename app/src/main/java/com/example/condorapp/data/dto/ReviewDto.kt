package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para reviews. Compatible con Firestore y Retrofit.
 *
 * Desnormalización (Firestore/NoSQL):
 *  - usuarioNombre: nombre del usuario embebido directamente en el review
 *  - articuloTitulo: título del artículo embebido directamente
 *
 * Cuando se usa Retrofit, los campos desnormalizados quedan vacíos y se usan
 * los objetos anidados (usuario, articulo).
 * Cuando se usa Firestore, los objetos anidados quedan nulos y se usan los
 * campos desnormalizados.
 */
data class ReviewDto(
    @SerializedName("id")             val id: String = "",
    @SerializedName("contenido")      val contenido: String = "",
    @SerializedName("calificacion")   val calificacion: Int = 0,
    @SerializedName("usuarioId")      val usuarioId: String = "",
    @SerializedName("articuloId")     val articuloId: String = "",
    @SerializedName("createdAt")      val createdAt: String = "",
    @SerializedName("updatedAt")      val updatedAt: String = "",
    // Objetos anidados (Retrofit — viene del backend con JOINs)
    @SerializedName("Usuario")        val usuario: UsuarioDto? = null,
    @SerializedName("Articulo")       val articulo: ArticuloDto? = null,
    // Campos desnormalizados (Firestore — NoSQL sin JOINs)
    @SerializedName("usuarioNombre")  val usuarioNombre: String = "",
    @SerializedName("articuloTitulo") val articuloTitulo: String = ""
)
