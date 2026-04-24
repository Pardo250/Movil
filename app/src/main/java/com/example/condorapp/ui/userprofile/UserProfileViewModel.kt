package com.example.condorapp.ui.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
 * ViewModel para la pantalla de perfil de otro usuario.
 * Carga sus datos, reviews, y maneja el estado de follow/unfollow.
 */
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
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
                _uiState.update { 
                    it.copy(
                        user = user,
                        followersCount = user.followersCount,
                        followingCount = user.followingCount
                    ) 
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "Error al cargar usuario")
                }
            }
            
            // Verificar si el usuario actual lo sigue
            val currentUid = authRepository.currentUser?.uid ?: ""
            if (currentUid.isNotEmpty() && currentUid != userId) {
                usuarioRepository.isFollowing(currentUid, userId).onSuccess { isFollowing ->
                    _uiState.update { it.copy(isFollowing = isFollowing) }
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

    /** Ejecuta la acción de seguir/dejar de seguir al usuario. */
    fun toggleFollow() {
        val targetUser = _uiState.value.user ?: return
        val currentUid = authRepository.currentUser?.uid ?: return
        if (currentUid == targetUser.id) return // No puede seguirse a sí mismo
        
        // Optimistic UI update
        val currentlyFollowing = _uiState.value.isFollowing
        _uiState.update { 
            it.copy(
                isFollowing = !currentlyFollowing,
                followersCount = if (currentlyFollowing) maxOf(0, it.followersCount - 1) else it.followersCount + 1
            ) 
        }
        
        viewModelScope.launch {
            val result = usuarioRepository.toggleFollow(currentUid, targetUser.id)
            result.onSuccess { nowFollowing ->
                // Sincronizar estado real con la UI
                _uiState.update { it.copy(isFollowing = nowFollowing) }
            }.onFailure {
                // Rollback en caso de error
                _uiState.update { 
                    it.copy(
                        isFollowing = currentlyFollowing,
                        followersCount = if (!currentlyFollowing) maxOf(0, it.followersCount - 1) else it.followersCount + 1
                    ) 
                }
            }
        }
    }
}
