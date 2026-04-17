package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.ArticuloDto

/**
 * Interfaz para la fuente de datos de artículos.
 * Permite intercambiar la implementación (Retrofit ↔ Firestore)
 * sin modificar repositorios ni capas superiores.
 */
interface ArticuloDataSource {
    suspend fun getAllArticulos(): List<ArticuloDto>
    suspend fun getArticuloById(id: String): ArticuloDto
}
