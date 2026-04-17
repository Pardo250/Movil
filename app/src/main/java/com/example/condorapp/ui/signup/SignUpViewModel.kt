package com.example.condorapp.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.R
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
 * ViewModel para la pantalla de registro. Gestiona los campos del formulario de creación de cuenta.
 * Al registrar exitosamente, guarda el perfil del usuario en Firestore.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onLastNameChange(lastName: String) {
        _uiState.update { it.copy(lastName = lastName) }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordErrorRes = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, passwordErrorRes = null) }
    }

    fun onSignUp() {
        val currentState = _uiState.value
        if (currentState.canSignUp) {
            if (currentState.password.length <= 7) {
                _uiState.update { it.copy(passwordErrorRes = R.string.error_password_too_short) }
                return
            }

            viewModelScope.launch {
                try {
                    // 1. Crear cuenta en Firebase Auth
                    val authResult = authRepository.signUp(currentState.email, currentState.password)
                    val uid = authResult.user?.uid
                        ?: throw Exception("No se pudo obtener el UID del usuario")

                    Log.d("SignUpViewModel", "Registro Auth exitoso -> ${currentState.email}, UID=$uid")

                    // 2. Guardar perfil del usuario en Firestore
                    val fullName = "${currentState.name} ${currentState.lastName}"
                    val saveResult = usuarioRepository.saveUsuario(
                        uid      = uid,
                        nombre   = fullName,
                        email    = currentState.email,
                        username = currentState.username
                    )

                    saveResult.onFailure { e ->
                        Log.e("SignUpViewModel", "Error guardando perfil en Firestore", e)
                    }

                    Log.d("SignUpViewModel", "Perfil guardado en Firestore -> $fullName")
                    _uiState.update { it.copy(isSignUpSuccessful = true, messageRes = null) }
                } catch (e: Exception) {
                    Log.e("SignUpViewModel", "Registro failed", e)
                    _uiState.update { it.copy(messageRes = R.string.error_signup) }
                }
            }
        }
    }

    fun onDismissMessage() {
        _uiState.update { it.copy(messageRes = null, passwordErrorRes = null) }
    }
}
