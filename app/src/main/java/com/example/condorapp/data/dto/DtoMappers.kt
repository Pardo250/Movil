package com.example.condorapp.data.dto

import com.example.condorapp.data.Articulo
import com.example.condorapp.data.Review
import com.example.condorapp.data.UserInfo

/**
 * Funciones de extensión para mapear DTOs de la capa de datos a modelos de la capa visual.
 *
 * Si el backend cambia el nombre de una columna, solo se modifica el DTO y este archivo;
 * la capa visual permanece intacta.
 */

/** Convierte un ArticuloDto a un Articulo de la capa visual. */
fun ArticuloDto.toArticulo(): Articulo = Articulo(
    id          = id,
    titulo      = titulo,
    descripcion = descripcion ?: "",
    tipo        = tipo
)

/** Convierte un ReviewDto a un Review de la capa visual. */
fun ReviewDto.toReview(): Review = Review(
    id         = id.toString(),
    name       = usuario?.nombre ?: "Usuario desconocido",
    rating     = calificacion,
    comment    = contenido,
    likes      = 0,
    usuarioId  = usuarioId
)

/** Convierte un UsuarioDto a un UserInfo de la capa visual. */
fun UsuarioDto.toUserInfo(): UserInfo = UserInfo(
    id     = id,
    nombre = nombre,
    email  = email
)
