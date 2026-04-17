package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para artículos. Compatible con Firestore (constructor vacío, IDs String)
 * y con Retrofit/Gson (@SerializedName).
 */
data class ArticuloDto(
    @SerializedName("id")          val id: String = "",
    @SerializedName("titulo")      val titulo: String = "",
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("tipo")        val tipo: String = "",
    @SerializedName("imagenUrl")   val imagenUrl: String? = null,
    @SerializedName("createdAt")   val createdAt: String = "",
    @SerializedName("updatedAt")   val updatedAt: String = ""
)
