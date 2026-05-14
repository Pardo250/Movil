package com.example.condorapp.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.Review
import com.example.condorapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadRecentReviews()
    }

    fun loadRecentReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = reviewRepository.getAllReviews()
            if (result.isSuccess) {
                val allReviews = result.getOrDefault(emptyList())
                val recentReviews = filterRecentReviews(allReviews)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        filteredReviews = recentReviews 
                    ) 
                }
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    ) 
                }
            }
        }
    }

    /**
     * Filtra las reviews publicadas en las últimas 24 horas.
     * Si no tienen fecha (o no se puede parsear), se consideran antiguas (se descartan).
     */
    internal fun filterRecentReviews(reviews: List<Review>): List<Review> {
        val now = Instant.now()
        val limit = now.minus(24, ChronoUnit.HOURS)

        return reviews.filter { review ->
            if (review.createdAt.isBlank()) return@filter false
            try {
                val reviewInstant = Instant.parse(review.createdAt)
                reviewInstant.isAfter(limit)
            } catch (e: DateTimeParseException) {
                // Formato no válido, se asume antiguo
                false
            }
        }
    }
}
