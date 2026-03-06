package com.example.condorapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.condorapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "LoginViewModel"

/**
 * ViewModel para la pantalla de Login. Gestiona el estado del formulario de inicio de sesión y las
 * acciones del usuario.
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /** Actualiza el campo de email y limpia mensajes. */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, messageRes = null) }
    }

    /** Actualiza el campo de contraseña y limpia mensajes. */
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, messageRes = null) }
    }

    /** Muestra mensaje de contraseña olvidada. */
    fun onForgotPassword() {
        _uiState.update { it.copy(messageRes = R.string.forgot_password) }
    }

    /**
     * Intenta iniciar sesión si los campos son válidos.
     * @return true si el login fue exitoso, false en caso contrario.
     */
    fun onSignIn(): Boolean {
        val currentState = _uiState.value
        return if (currentState.canSignIn) {
            Log.d(TAG, "Login correcto -> ${currentState.email}")
            _uiState.update { it.copy(messageRes = R.string.sign_in) }
            true
        } else {
            false
        }
    }

    /** Muestra mensaje de acción de Google. */
    fun onContinueGoogle() {
        _uiState.update { it.copy(messageRes = R.string.continue_with_google) }
    }

    /** Muestra mensaje de registro y registra en log. */
    fun onRegister() {
        Log.d(TAG, "Registro -> ${_uiState.value.email}")
        _uiState.update { it.copy(messageRes = R.string.register) }
    }

    /** Oculta el mensaje de feedback. */
    fun onDismissMessage() {
        _uiState.update { it.copy(messageRes = null) }
    }
}
