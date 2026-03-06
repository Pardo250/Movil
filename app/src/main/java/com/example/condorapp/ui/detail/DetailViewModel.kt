package com.example.condorapp.ui.detail

import androidx.lifecycle.ViewModel
import com.example.condorapp.R
import com.example.condorapp.data.Review
import com.example.condorapp.data.local.PostRepository
import com.example.condorapp.data.local.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de detalle de un destino. Carga datos del post y las reseñas de la
 * comunidad.
 */
class DetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    /** Carga los datos de un post específico y sus reseñas. */
    fun loadPostDetail(postId: String) {
        val postData = PostRepository.getPosts().find { it.location == postId }
        val reviews = ReviewRepository.getReviews()

        _uiState.update {
            it.copy(
                    title = postData?.location ?: postId,
                    location = postData?.user ?: "Destino Condorapp",
                    imageRes = postData?.imageRes ?: R.drawable.valle_del_cocora,
                    reviews = reviews
            )
        }
    }

    /** Incrementa los likes de una reseña específica. */
    fun onLikeReview(review: Review) {
        _uiState.update { currentState ->
            val updatedReviews =
                    currentState.reviews.map {
                        if (it == review) it.copy(likes = it.likes + 1) else it
                    }
            currentState.copy(reviews = updatedReviews)
        }
    }
}
