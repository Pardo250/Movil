package com.example.condorapp.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.FeedPlace
import com.example.condorapp.data.repository.ArticuloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de exploración (Feed).
 * Carga los artículos desde la API REST via ArticuloRepository.
 */
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadArticulos()
    }

    /** Carga artículos desde el backend y los mapea a FeedPlace. */
    fun loadArticulos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = articuloRepository.getAllArticulos()

            result.onSuccess { articulos ->
                val places = articulos.map { articulo ->
                    FeedPlace(
                        imageUrl = "",
                        location = articulo.titulo
                    )
                }
                _uiState.update { it.copy(places = places, isLoading = false) }
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
}
