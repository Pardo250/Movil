package com.example.condorapp.data.dto

import com.google.firebase.firestore.PropertyName

/**
 * DTO para artículos. Compatible con Firestore y Retrofit.
 *
 * NOTA sobre timestamps: Firestore puede guardar createdAt/updatedAt como Timestamp objects.
 * Para evitar el error "Can't convert object of type ... to String", ignoramos esos campos
 * usando @get:Exclude/@set:Exclude de Firestore, o simplemente no los mapeamos en el DTO.
 * Los timestamps no se usan en la UI, así que se omiten aquí.
 */
data class ArticuloDto(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String? = null,
    val tipo: String = "",
    val imagenUrl: String? = null
)
