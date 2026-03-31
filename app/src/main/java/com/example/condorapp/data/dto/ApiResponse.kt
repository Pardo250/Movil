package com.example.condorapp.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Wrapper genérico para todas las respuestas del backend.
 * El servidor siempre retorna: { "success": true/false, "data": ..., "message": ... }
 */
data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data")    val data: T? = null,
    @SerializedName("message") val message: String? = null
)
