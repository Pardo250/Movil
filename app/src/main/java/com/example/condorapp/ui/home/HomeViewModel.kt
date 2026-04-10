package com.example.condorapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.repository.ArticuloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla Home. Carga los artículos desde el backend via ArticuloRepository.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadArticulos()
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
}
