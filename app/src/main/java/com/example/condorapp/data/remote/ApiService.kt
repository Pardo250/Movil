package com.example.condorapp.data.remote

import com.example.condorapp.data.dto.ApiResponse
import com.example.condorapp.data.dto.ArticuloDto
import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.example.condorapp.data.dto.UsuarioDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ─── ARTÍCULOS ─────────────────────────────────────────────────────────────
    // GET /articulos
    @GET("articulos")
    suspend fun getAllArticulos(): Response<ApiResponse<List<ArticuloDto>>>

    // GET /articulos/:id
    @GET("articulos/{id}")
    suspend fun getArticuloById(@Path("id") id: Int): Response<ApiResponse<ArticuloDto>>

    // ─── USUARIOS ──────────────────────────────────────────────────────────────
    // GET /usuarios
    @GET("usuarios")
    suspend fun getAllUsuarios(): Response<ApiResponse<List<UsuarioDto>>>

    // GET /usuarios/:id
    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): Response<ApiResponse<UsuarioDto>>

    // ─── REVIEWS ───────────────────────────────────────────────────────────────
    // POST /reviews
    @POST("reviews")
    suspend fun createReview(@Body body: CreateReviewDto): Response<ApiResponse<ReviewDto>>

    // GET /reviews/articulo/:articuloId
    @GET("reviews/articulo/{articuloId}")
    suspend fun getReviewsByArticulo(@Path("articuloId") articuloId: Int): Response<ApiResponse<List<ReviewDto>>>

    // GET /reviews/usuario/:usuarioId
    @GET("reviews/usuario/{usuarioId}")
    suspend fun getReviewsByUsuario(@Path("usuarioId") usuarioId: Int): Response<ApiResponse<List<ReviewDto>>>

    // PUT /reviews/:id
    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Int,
        @Body body: UpdateReviewDto
    ): Response<ApiResponse<ReviewDto>>

    // DELETE /reviews/:id
    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
