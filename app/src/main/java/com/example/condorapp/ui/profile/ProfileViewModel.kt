package com.example.condorapp.ui.profile

import androidx.lifecycle.ViewModel
import com.example.condorapp.data.local.UserProfileRepository
import com.example.condorapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel para la pantalla de perfil del usuario. Carga los datos del perfil desde el repositorio
 * local y foto desde FirebaseAuth.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
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
}
