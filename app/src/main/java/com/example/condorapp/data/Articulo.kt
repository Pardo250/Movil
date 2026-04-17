package com.example.condorapp.data

data class Articulo(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val imagenUrl: String = ""
)
