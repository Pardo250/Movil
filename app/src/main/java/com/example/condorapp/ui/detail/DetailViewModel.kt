package com.example.condorapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.Review
import com.example.condorapp.data.repository.ArticuloRepository
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de un artículo.
 * Carga el artículo y escucha sus reseñas en tiempo real desde Firestore.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val currentUserId: String
        get() = authRepository.currentUser?.uid ?: ""

    private var reviewsJob: Job? = null

    /**
     * Carga los datos de un artículo e inicia la escucha de sus reseñas.
     * @param postId ID del artículo (String — compatible con Firestore y Retrofit)
     */
    fun loadPostDetail(postId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, articuloId = postId) }

            // Cargar artículo
            val articuloResult = articuloRepository.getArticuloById(postId)
            articuloResult.onSuccess { articulo ->
                _uiState.update {
                    it.copy(
                        title       = articulo.titulo,
                        location    = articulo.tipo,
                        imageUrl    = articulo.imagenUrl,
                        description = articulo.descripcion.ifBlank {
                            "Descubre este increíble contenido, lleno de detalles y experiencias que te encantarán."
                        }
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(title = "Artículo", location = "Sin conexión")
                }
            }

            // Cancelar escucha anterior si existe
            reviewsJob?.cancel()

            // Escuchar reviews del artículo en tiempo real (Flow)
            reviewsJob = viewModelScope.launch {
                reviewRepository.listenReviewsByArticulo(postId).collect { result ->
                    result.onSuccess { reviews ->
                        // Para cada review, verificar si el usuario actual le dio like
                        val uid = currentUserId
                        val likedIds = mutableSetOf<String>()
                        if (uid.isNotEmpty()) {
                            reviews.forEach { review ->
                                val liked = reviewRepository.isLikedByUser(review.id, uid).getOrDefault(false)
                                if (liked) likedIds.add(review.id)
                            }
                        }

                        // Mapear reviews marcando cuáles tienen like
                        val mappedReviews = reviews.map { it.copy(likedByMe = likedIds.contains(it.id)) }
                        _uiState.update { it.copy(reviews = mappedReviews, likedReviewIds = likedIds, isLoading = false) }
                    }.onFailure { error ->
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar reseñas")
                        }
                    }
                }
            }
        }
    }

    /** Realiza toggle del like de una reseña usando transacciones de Firestore. */
    fun onLikeReview(review: Review) {
        val uid = currentUserId
        if (uid.isEmpty()) return

        // Optimistic UI update
        val currentlyLiked = _uiState.value.likedReviewIds.contains(review.id)
        val newLikedIds = if (currentlyLiked) {
            _uiState.value.likedReviewIds - review.id
        } else {
            _uiState.value.likedReviewIds + review.id
        }
        
        _uiState.update { currentState ->
            val updatedReviews = currentState.reviews.map {
                if (it.id == review.id) {
                    val newLikes = if (currentlyLiked) maxOf(0, it.likes - 1) else it.likes + 1
                    it.copy(likes = newLikes, likedByMe = !currentlyLiked)
                } else it
            }
            currentState.copy(reviews = updatedReviews, likedReviewIds = newLikedIds)
        }

        // Backend update
        viewModelScope.launch {
            reviewRepository.toggleLike(review.id, uid)
            // No necesitamos revertir el optimistic update en onFailure porque
            // el Flow de listenReviewsByArticulo emitirá el estado real automáticamente
        }
    }
}
