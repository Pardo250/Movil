package com.example.condorapp.ui.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
 * ViewModel para la pantalla de perfil de otro usuario.
 * Carga sus datos y reviews desde Firestore.
 */
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        val userId = savedStateHandle.get<String>("userId") ?: ""
        if (userId.isNotEmpty()) {
            loadUserProfile(userId)
        }
    }

    /** Carga el perfil del usuario y sus reviews. */
    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Cargar datos del usuario
            val userResult = usuarioRepository.getUsuarioById(userId)
            userResult.onSuccess { user ->
                _uiState.update { it.copy(user = user) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "Error al cargar usuario")
                }
            }

            // Cargar reviews del usuario
            val reviewsResult = reviewRepository.getReviewsByUsuario(userId)
            reviewsResult.onSuccess { reviews ->
                _uiState.update { it.copy(reviews = reviews, isLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar reviews")
                }
            }
        }
    }
}
