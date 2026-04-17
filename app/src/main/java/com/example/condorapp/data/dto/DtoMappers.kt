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
    tipo        = tipo,
    imagenUrl   = imagenUrl ?: ""
)

/**
 * Convierte un ReviewDto a un Review de la capa visual.
 * Soporta tanto Retrofit (objetos anidados) como Firestore (campos desnormalizados).
 */
fun ReviewDto.toReview(): Review = Review(
    id              = id,
    name            = usuario?.nombre
                        ?: usuarioNombre.ifEmpty { "Usuario desconocido" },
    rating          = calificacion,
    comment         = contenido,
    likes           = 0,
    usuarioId       = usuarioId,
    articuloNombre  = articulo?.titulo
                        ?: articuloTitulo.ifEmpty { "" }
)

/** Convierte un UsuarioDto a un UserInfo de la capa visual. */
fun UsuarioDto.toUserInfo(): UserInfo = UserInfo(
    id        = id,
    nombre    = nombre,
    email     = email,
    username  = username.ifEmpty { "@${nombre.lowercase().replace(" ", "_")}" },
    bio       = bio.ifEmpty { "Sin biografía" },
    avatarUrl = avatarUrl
)
