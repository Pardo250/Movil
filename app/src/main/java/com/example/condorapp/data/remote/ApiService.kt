package com.example.condorapp.data.remote

import com.example.condorapp.data.dto.ArticuloDto
import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.example.condorapp.data.dto.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ─── ARTÍCULOS ─────────────────────────────────────────────────────────────
    @GET("articulos")
    suspend fun getAllArticulos(): List<ArticuloDto>

    @GET("articulos/{id}")
    suspend fun getArticuloById(@Path("id") id: Int): ArticuloDto

    // ─── USUARIOS ──────────────────────────────────────────────────────────────
    @GET("usuarios")
    suspend fun getAllUsuarios(): List<UsuarioDto>

    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): UsuarioDto

    // ─── REVIEWS ───────────────────────────────────────────────────────────────
    @POST("reviews")
    suspend fun createReview(@Body body: CreateReviewDto): ReviewDto

    @GET("reviews/articulo/{articuloId}")
    suspend fun getReviewsByArticulo(@Path("articuloId") articuloId: Int): List<ReviewDto>

    @GET("reviews/usuario/{usuarioId}")
    suspend fun getReviewsByUsuario(@Path("usuarioId") usuarioId: Int): List<ReviewDto>

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Int,
        @Body body: UpdateReviewDto
    ): ReviewDto

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: Int): Unit
}
