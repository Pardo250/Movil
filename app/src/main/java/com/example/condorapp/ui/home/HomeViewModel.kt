package com.example.condorapp.ui.home

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
 * ViewModel para la pantalla Home. Carga los artículos desde el backend via ArticuloRepository.
 * Permite filtrar el feed de reviews entre "Todos" y "Siguiendo".
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository,
    private val reviewRepository: ReviewRepository,
    private val usuarioRepository: UsuarioRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadArticulos()
        loadReviews()
    }

    /** Carga la lista de artículos desde el backend. */
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

    /** Marca un artículo como seleccionado por su índice. */
    fun onArticuloSelected(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    /**
     * Alterna el filtro entre "Todos" y "Siguiendo" y recarga reviews.
     */
    fun onToggleFilter(showFollowingOnly: Boolean) {
        _uiState.update { it.copy(showFollowingOnly = showFollowingOnly) }
        loadReviews()
    }

    /**
     * Carga reviews. Si showFollowingOnly=true, filtra solo los de usuarios seguidos.
     */
    private fun loadReviews() {
        viewModelScope.launch {
            val currentUid = authRepository.currentUser?.uid ?: ""
            val state = _uiState.value

            reviewRepository.getAllReviews().onSuccess { allReviews ->
                if (state.showFollowingOnly && currentUid.isNotEmpty()) {
                    usuarioRepository.getFollowingIds(currentUid).onSuccess { followingIds ->
                        val filtered = allReviews.filter { it.usuarioId in followingIds }
                        _uiState.update { it.copy(reviews = filtered) }
                    }.onFailure {
                        _uiState.update { it.copy(reviews = allReviews) }
                    }
                } else {
                    _uiState.update { it.copy(reviews = allReviews) }
                }
            }
        }
    }
}
