package com.example.condorapp.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.repository.ArticuloRepository
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.ReviewRepository
import com.example.condorapp.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de exploración (Feed).
 * Carga artículos y reviews globales, y permite filtrar reviews por usuarios seguidos.
 */
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository,
    private val reviewRepository: ReviewRepository,
    private val usuarioRepository: UsuarioRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadArticulos()
        loadReviews()
    }

    /** Carga todos los artículos del backend. */
    fun loadArticulos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = articuloRepository.getAllArticulos()

            result.onSuccess { articulos ->
                _uiState.update { it.copy(articulos = articulos, isLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error desconocido")
                }
            }
        }
    }

    /** Actualiza la categoría seleccionada. */
    fun onCategorySelected(index: Int) {
        _uiState.update { it.copy(selectedCategoryIndex = index) }
    }

    /** 
     * Alterna el filtro entre "Todos" y "Siguiendo".
     * Recarga los reviews aplicando el filtro adecuado.
     */
    fun onToggleFilter(showFollowingOnly: Boolean) {
        _uiState.update { it.copy(showFollowingOnly = showFollowingOnly) }
        loadReviews()
    }

    /**
     * Carga las reviews. Si showFollowingOnly es true, filtra por los usuarios
     * que el usuario actual sigue.
     */
    private fun loadReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val state = _uiState.value
            val currentUid = authRepository.currentUser?.uid ?: ""

            // 1. Obtener todas las reviews
            val reviewsResult = reviewRepository.getAllReviews()
            
            reviewsResult.onSuccess { allReviews ->
                if (state.showFollowingOnly && currentUid.isNotEmpty()) {
                    // 2. Obtener IDs de usuarios seguidos
                    usuarioRepository.getFollowingIds(currentUid).onSuccess { followingIds ->
                        val filteredReviews = allReviews.filter { it.usuarioId in followingIds }
                        _uiState.update { it.copy(reviews = filteredReviews, isLoading = false) }
                    }.onFailure { error ->
                        _uiState.update { 
                            it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar filtro") 
                        }
                    }
                } else {
                    // Mostrar todas
                    _uiState.update { it.copy(reviews = allReviews, isLoading = false) }
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar reviews")
                }
            }
        }
    }
}
