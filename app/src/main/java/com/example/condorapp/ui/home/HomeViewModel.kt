package com.example.condorapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.condorapp.data.local.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla Home. Carga los posts desde el repositorio y gestiona la selección de
 * posts.
 */
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    /** Carga la lista de posts desde el repositorio local. */
    private fun loadPosts() {
        val posts = PostRepository.getPosts()
        _uiState.update { it.copy(posts = posts) }
    }

    /** Marca un post como seleccionado por su índice. */
    fun onPostSelected(index: Int) {
        _uiState.update { it.copy(selectedPostIndex = index) }
    }
}
