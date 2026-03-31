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
                    imageRes  = postData?.imageRes ?: com.example.condorapp.R.drawable.valle_del_cocora,
                    isLoading = true,
                    errorMessage = null
                )
            }
            try {
                // Intenta obtener articuloId numérico; si no, carga todas las reviews
                val articuloId = postId.toIntOrNull() ?: 1
                val reviews = reviewRepository.getReviewsByArticulo(articuloId)
                _uiState.update { it.copy(reviews = reviews, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Error al cargar reseñas")
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
}
