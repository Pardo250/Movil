package com.example.condorapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.Review
import com.example.condorapp.data.repository.ArticuloRepository
import com.example.condorapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de un artículo.
 * Carga el artículo y sus reseñas desde el backend.
 * Nota: Editar/eliminar reviews se hace desde ProfileViewModel.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos de un artículo y sus reseñas desde el backend.
     * @param postId ID del artículo (String que se parsea a Int)
     */
    fun loadPostDetail(postId: String) {
        val articuloId = postId.toIntOrNull() ?: 1

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, articuloId = articuloId) }

            // Cargar artículo del backend
            val articuloResult = articuloRepository.getArticuloById(articuloId)
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
                    it.copy(title = "Artículo #$articuloId", location = "Sin conexión")
                }
            }

            // Cargar reviews del artículo
            val reviewsResult = reviewRepository.getReviewsByArticulo(articuloId)
            reviewsResult.onSuccess { reviews ->
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
}
