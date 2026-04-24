package com.example.condorapp.ui.followlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de lista de Seguidores/Seguidos.
 * Carga ambas listas para un usuario específico y permite hacer toggleFollow.
 */
@HiltViewModel
class FollowListViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowListUiState())
    val uiState: StateFlow<FollowListUiState> = _uiState.asStateFlow()

    init {
        val userId = savedStateHandle.get<String>("userId") ?: ""
        if (userId.isNotEmpty()) {
            loadLists(userId)
        }
    }

    /** Carga seguidores, seguidos y los IDs que sigue el usuario actual (para botones). */
    fun loadLists(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Cargar followers
                val followersResult = usuarioRepository.getFollowers(userId)
                val followers = followersResult.getOrNull() ?: emptyList()

                // Cargar following
                val followingResult = usuarioRepository.getFollowing(userId)
                val following = followingResult.getOrNull() ?: emptyList()

                // Obtener los IDs que el usuario actual sigue (para pintar el botón "Siguiendo" o "Seguir")
                val currentUid = authRepository.currentUser?.uid ?: ""
                val myFollowingIds = if (currentUid.isNotEmpty()) {
                    usuarioRepository.getFollowingIds(currentUid).getOrNull()?.toSet() ?: emptySet()
                } else {
                    emptySet()
                }

                _uiState.update { 
                    it.copy(
                        followers = followers, 
                        following = following, 
                        myFollowingIds = myFollowingIds,
                        isLoading = false
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Error al cargar listas")
                }
            }
        }
    }

    /** Cambia el tab seleccionado (0: Seguidores, 1: Siguiendo) */
    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    /** Alterna el estado de seguimiento para un usuario de la lista. */
    fun toggleFollow(targetUserId: String) {
        val currentUid = authRepository.currentUser?.uid ?: return
        if (currentUid == targetUserId) return

        // Optimistic UI update
        val currentlyFollowing = _uiState.value.myFollowingIds.contains(targetUserId)
        val newFollowingIds = if (currentlyFollowing) {
            _uiState.value.myFollowingIds - targetUserId
        } else {
            _uiState.value.myFollowingIds + targetUserId
        }
        
        _uiState.update { it.copy(myFollowingIds = newFollowingIds) }

        // Backend update
        viewModelScope.launch {
            val result = usuarioRepository.toggleFollow(currentUid, targetUserId)
            result.onSuccess { nowFollowing ->
                // Sincronizar estado real con la UI
                val finalFollowingIds = if (nowFollowing) {
                    _uiState.value.myFollowingIds + targetUserId
                } else {
                    _uiState.value.myFollowingIds - targetUserId
                }
                _uiState.update { it.copy(myFollowingIds = finalFollowingIds) }
            }.onFailure {
                // Rollback en caso de error
                val rollbackIds = if (currentlyFollowing) {
                    _uiState.value.myFollowingIds + targetUserId
                } else {
                    _uiState.value.myFollowingIds - targetUserId
                }
                _uiState.update { it.copy(myFollowingIds = rollbackIds) }
            }
        }
    }
}
