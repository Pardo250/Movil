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
import javax.inject.Inject

/**
 * ViewModel para la pantalla del mapa de reviews (Sprint 13.5).
 *
 * Responsabilidades:
 * - Cargar reviews de las últimas 24 horas con ubicación (filtrado en Repository/DataSource).
 * - Exponer StateFlow con la lista ya procesada (NO se filtra en la UI).
 * - Gestionar selección de marcador para el BottomSheet.
 * - Permitir refresco manual de reviews.
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadReviews()
    }

    /**
     * Carga reviews de las últimas 24 horas con coordenadas válidas.
     * Todo el filtrado se realiza en la capa de datos (DataSource + Repository).
     */
    fun loadReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            reviewRepository.getReviewsForMap()
                .onSuccess { reviews ->
                    _uiState.update {
                        it.copy(isLoading = false, reviews = reviews)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al cargar reviews"
                        )
                    }
                }
        }
    }

    /** Refresco manual de reviews (botón FAB). */
    fun refreshReviews() {
        loadReviews()
    }

    /** Selecciona un review al clickear su marcador en el mapa. */
    fun onMarkerClick(review: Review) {
        _uiState.update { it.copy(selectedReview = review) }
    }

    /** Cierra el BottomSheet de detalle. */
    fun onDismissSheet() {
        _uiState.update { it.copy(selectedReview = null) }
    }

    /** Limpia el mensaje de error después de mostrarlo. */
    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
