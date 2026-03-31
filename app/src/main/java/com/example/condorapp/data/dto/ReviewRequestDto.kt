package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

/** Body para POST /reviews */
data class CreateReviewDto(
    @SerializedName("contenido")    val contenido: String,
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("usuarioId")    val usuarioId: Int,
    @SerializedName("articuloId")   val articuloId: Int
)

/** Body para PUT /reviews/:id */
data class UpdateReviewDto(
    @SerializedName("contenido")    val contenido: String? = null,
    @SerializedName("calificacion") val calificacion: Int? = null
)
