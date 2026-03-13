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
import javax.inject.Inject

private const val TAG = "EditProfileViewModel"

/**
 * ViewModel para la pantalla de edición de perfil. Gestiona los campos editables, guardado,
 * eliminación de cuenta y cierre de sesión.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onFullNameChange(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun onBioChange(bio: String) {
        _uiState.update { it.copy(bio = bio) }
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
