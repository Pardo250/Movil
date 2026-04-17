package com.example.condorapp.data.remote

import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ApiResponseUnwrapInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        // Solo procesamos respuestas HTTP exitosas con body
        if (!response.isSuccessful) return response

        val body = response.body ?: return response
        val contentType = body.contentType()
        val json = body.string()

        return try {
            val jsonObject = JsonParser.parseString(json).asJsonObject
            val success = jsonObject.get("success")?.asBoolean ?: true

            if (!success) {
                // El backend respondió con success=false → generamos error HTTP
                val message = jsonObject.get("message")?.asString ?: "Error del servidor"
                response.newBuilder()
                    .code(500)
                    .message(message)
                    .body(json.toResponseBody(contentType))
                    .build()
            } else {
                // Extrae solo el campo "data" para que Gson lo deserialice como DTO
                val dataElement = jsonObject.get("data")
                val newJson = dataElement?.toString() ?: "null"
                response.newBuilder()
                    .body(newJson.toResponseBody(contentType))
                    .build()
            }
        } catch (_: Exception) {
            // Si no es JSON válido o falla el parseo, devuelve la respuesta tal cual
            response.newBuilder()
                .body(json.toResponseBody(contentType))
                .build()
        }
    }
}
