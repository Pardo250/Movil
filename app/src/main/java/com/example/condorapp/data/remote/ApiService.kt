package com.example.condorapp.data.remote

import com.example.condorapp.data.dto.ApiResponse
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

/**
 * Interfaz de servicio Retrofit.
 *
 * Buena práctica: los métodos retornan directamente ApiResponse<T> en vez de
 * Response<ApiResponse<T>>. Retrofit lanza HttpException automáticamente para
 * códigos HTTP no exitosos (4xx, 5xx), así que el manejo de errores HTTP
 * queda en los DataSources/Repositories con try-catch.
 */
interface ApiService {

    // ─── ARTÍCULOS ─────────────────────────────────────────────────────────────
    @GET("articulos")
    suspend fun getAllArticulos(): ApiResponse<List<ArticuloDto>>

    @GET("articulos/{id}")
    suspend fun getArticuloById(@Path("id") id: Int): ApiResponse<ArticuloDto>

    // ─── USUARIOS ──────────────────────────────────────────────────────────────
    @GET("usuarios")
    suspend fun getAllUsuarios(): ApiResponse<List<UsuarioDto>>

    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): ApiResponse<UsuarioDto>

    // ─── REVIEWS ───────────────────────────────────────────────────────────────
    @POST("reviews")
    suspend fun createReview(@Body body: CreateReviewDto): ApiResponse<ReviewDto>

    @GET("reviews/articulo/{articuloId}")
    suspend fun getReviewsByArticulo(@Path("articuloId") articuloId: Int): ApiResponse<List<ReviewDto>>

    @GET("reviews/usuario/{usuarioId}")
    suspend fun getReviewsByUsuario(@Path("usuarioId") usuarioId: Int): ApiResponse<List<ReviewDto>>

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Int,
        @Body body: UpdateReviewDto
    ): ApiResponse<ReviewDto>

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: Int): ApiResponse<Unit>
}
