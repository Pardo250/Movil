package com.example.condorapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.Review
import com.example.condorapp.data.local.PostRepository
import com.example.condorapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de un destino.
 * Carga los datos del post localmente y las reseñas desde la API REST.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos de un post específico y sus reseñas desde el backend.
     * @param postId ID del artículo en la API (Int convertido desde String)
     */
    fun loadPostDetail(postId: String) {
        // Datos visuales locales (imágenes, etc.)
        val postData = PostRepository.getPosts().find { it.location == postId }

        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    title     = postData?.location ?: postId,
                    location  = postData?.user ?: "Destino Condorapp",
                    imageUrl  = "",
                    isLoading = true,
                    errorMessage = null
                )
            }
            // Intenta obtener articuloId numérico; si no, carga todas las reviews
            val articuloId = postId.toIntOrNull() ?: 1
            val result = reviewRepository.getReviewsByArticulo(articuloId)

            result.onSuccess { reviews ->
                _uiState.update { it.copy(reviews = reviews, isLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar reseñas")
                }
            }
        }
    }

    /** Incrementa los likes de una reseña específica (solo local). */
    fun onLikeReview(review: Review) {
        _uiState.update { currentState ->
            val updatedReviews = currentState.reviews.map {
                if (it == review) it.copy(likes = it.likes + 1) else it
            }
            currentState.copy(reviews = updatedReviews)
        }
    }

    /**
     * Elimina una reseña. Aplica filtro optimista de la lista local para dar
     * respuesta visual inmediata sin necesidad de un nuevo GET al backend.
     */
    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            val id = reviewId.toIntOrNull() ?: return@launch
            val result = reviewRepository.deleteReview(id)

            result.onSuccess {
                // Estándar de UX: filtrar la lista local en vez de re-consultar
                _uiState.update { currentState ->
                    currentState.copy(
                        reviews = currentState.reviews.filter { it.id != reviewId }
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "Error al eliminar reseña")
                }
            }
        }
    }
}
