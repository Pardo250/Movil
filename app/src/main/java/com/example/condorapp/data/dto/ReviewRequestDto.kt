package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Body para POST /reviews (Retrofit) o .add() (Firestore).
 * Incluye usuarioNombre para desnormalización en Firestore.
 * Retrofit ignora el campo extra.
 */
data class CreateReviewDto(
    @SerializedName("contenido")      val contenido: String = "",
    @SerializedName("calificacion")   val calificacion: Int = 0,
    @SerializedName("usuarioId")      val usuarioId: String = "",
    @SerializedName("articuloId")     val articuloId: String = "",
    @SerializedName("usuarioNombre")  val usuarioNombre: String = ""
)

/** Body para PUT /reviews/:id (Retrofit) o .set() merge (Firestore). */
data class UpdateReviewDto(
    @SerializedName("contenido")    val contenido: String? = null,
    @SerializedName("calificacion") val calificacion: Int? = null
)
