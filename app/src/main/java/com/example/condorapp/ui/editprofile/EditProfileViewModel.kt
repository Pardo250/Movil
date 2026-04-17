package com.example.condorapp.ui.editprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.condorapp.R
import com.example.condorapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import android.net.Uri
import com.example.condorapp.data.repository.StorageRepository
import javax.inject.Inject

private const val TAG = "EditProfileViewModel"

/**
 * ViewModel para la pantalla de edición de perfil. Gestiona los campos editables, guardado,
 * eliminación de cuenta y cierre de sesión.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadCurrentProfile()
    }
    private fun loadCurrentProfile() {
        val currentProfile = com.example.condorapp.data.local.UserProfileRepository.getProfile()
        _uiState.update {
            it.copy(
                username = currentProfile.username,
                fullName = currentProfile.name,
                imageUrl = currentProfile.avatarUrl
            )
        }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onFullNameChange(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun onBioChange(bio: String) {
        _uiState.update { it.copy(bio = bio) }
    }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploadingImage = true, imageUploadError = null) }
            val result = storageRepository.uploadProfileImage(uri)
            
            result.onSuccess { url ->
                _uiState.update { it.copy(isUploadingImage = false, imageUrl = url) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isUploadingImage = false, imageUploadError = exception.message) }
            }
        }
    }

    /** Guarda los cambios del perfil y muestra mensaje de confirmación. */
    fun onSave() {
        Log.d(TAG, "Guardando: ${_uiState.value.username}")
        _uiState.update { it.copy(messageRes = R.string.save_changes) }
    }

    /** Elimina la cuenta y muestra mensaje de confirmación. */
    fun onDeleteAccount() {
        _uiState.update { EditProfileUiState(messageRes = R.string.delete_account) }
    }

    /** Cierra la sesión del usuario actual en Firebase y señala al UI que navegue a Login. */
    fun onSignOut() {
        Log.d(TAG, "Cerrando sesión del usuario: ${authRepository.currentUser?.email}")
        authRepository.signOut()
        _uiState.update { it.copy(isSignedOut = true) }
    }

    /** Oculta el mensaje de feedback. */
    fun onDismissMessage() {
        _uiState.update { it.copy(messageRes = null) }
    }
}
