package com.example.condorapp.ui.feed

import androidx.lifecycle.ViewModel
import com.example.condorapp.data.local.FeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de exploración (Feed). Carga los lugares desde el repositorio y
 * gestiona la categoría seleccionada.
 */
class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadPlaces()
    }

    /** Carga la lista de lugares desde el repositorio local. */
    private fun loadPlaces() {
        val places = FeedRepository.getPlaces()
        _uiState.update { it.copy(places = places) }
    }

    /** Actualiza la categoría seleccionada. */
    fun onCategorySelected(index: Int) {
        _uiState.update { it.copy(selectedCategoryIndex = index) }
    }
}
