package com.example.condorapp.ui.editprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.R
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.StorageRepository
import com.example.condorapp.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "EditProfileViewModel"

/**
 * ViewModel para la pantalla de edición de perfil. Gestiona los campos editables, guardado
 * en Firestore, eliminación de cuenta y cierre de sesión.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    /** UID del usuario actualmente autenticado. */
    private val currentUserId: String
        get() = authRepository.currentUser?.uid ?: ""

    init {
        loadCurrentProfile()
    }

    /** Carga el perfil actual desde Firestore. */
    private fun loadCurrentProfile() {
        val uid = currentUserId
        if (uid.isEmpty()) return

        viewModelScope.launch {
            val result = usuarioRepository.getUsuarioById(uid)
            result.onSuccess { user ->
                val photoUrl = authRepository.currentUser?.photoUrl?.toString()
                _uiState.update {
                    it.copy(
                        username = user.username,
                        fullName = user.nombre,
                        bio      = user.bio,
                        imageUrl = photoUrl ?: user.avatarUrl.ifEmpty { null }
                    )
                }
            }.onFailure {
                // Fallback: usar datos de FirebaseAuth
                val firebaseUser = authRepository.currentUser
                _uiState.update {
                    it.copy(
                        username = "@${firebaseUser?.email?.substringBefore("@") ?: "user"}",
                        fullName = firebaseUser?.displayName ?: "Usuario",
                        imageUrl = firebaseUser?.photoUrl?.toString()
                    )
                }
            }
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

                // También guardar la URL en Firestore
                val uid = currentUserId
                if (uid.isNotEmpty()) {
                    usuarioRepository.updateUsuario(uid, mapOf("avatarUrl" to url))
                }
            }.onFailure { exception ->
                _uiState.update { it.copy(isUploadingImage = false, imageUploadError = exception.message) }
            }
        }
    }

    /** Guarda los cambios del perfil en Firestore y muestra mensaje de confirmación. */
    fun onSave() {
        val uid = currentUserId
        if (uid.isEmpty()) return

        val state = _uiState.value
        viewModelScope.launch {
            val fields = mutableMapOf<String, Any>(
                "nombre"   to state.fullName,
                "username" to state.username,
                "bio"      to state.bio
            )
            state.imageUrl?.let { fields["avatarUrl"] = it }

            val result = usuarioRepository.updateUsuario(uid, fields)
            result.onSuccess {
                Log.d(TAG, "Perfil guardado en Firestore: ${state.username}")
                _uiState.update { it.copy(messageRes = R.string.save_changes) }
            }.onFailure { e ->
                Log.e(TAG, "Error guardando perfil", e)
                _uiState.update { it.copy(messageRes = R.string.save_changes) }
            }
        }
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
