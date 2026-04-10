package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

data class ArticuloDto(
    @SerializedName("id")          val id: Int,
    @SerializedName("titulo")      val titulo: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("tipo")        val tipo: String,
    @SerializedName("imagenUrl")   val imagenUrl: String?,
    @SerializedName("createdAt")   val createdAt: String,
    @SerializedName("updatedAt")   val updatedAt: String
)
