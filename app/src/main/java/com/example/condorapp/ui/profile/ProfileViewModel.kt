package com.example.condorapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.local.UserProfileRepository
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de perfil del usuario. Carga los datos del perfil desde el repositorio
 * local, foto desde FirebaseAuth, y las reviews desde el backend.
 *
 * CURRENT_USER_ID = 1 → ID del usuario "quemado" según la guía del Sprint.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    companion object {
        const val CURRENT_USER_ID = 1
    }

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
        loadMyReviews()
    }

    /** Carga los datos del perfil del usuario desde el repositorio local. */
    private fun loadProfile() {
        val profile = UserProfileRepository.getProfile()
        val photoUrl = authRepository.currentUser?.photoUrl?.toString()
        _uiState.update {
            it.copy(
                name = profile.name,
                username = profile.username,
                imageUrl = photoUrl ?: profile.avatarUrl,
                photos = profile.photos
            )
        }
    }

    /** Carga las reviews que ha realizado el usuario actual (id=1) desde el backend. */
    private fun loadMyReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = reviewRepository.getReviewsByUsuario(CURRENT_USER_ID)

            result.onSuccess { reviews ->
                _uiState.update { it.copy(reviews = reviews, isLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar reviews")
                }
            }
        }
    }
}
