package com.example.condorapp.ui.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.condorapp.R
import com.example.condorapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de registro. Gestiona los campos del formulario de creación de cuenta.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
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
                    authRepository.signUp(currentState.email, currentState.password)
                    Log.d("SignUpViewModel", "Registro exitoso -> ${currentState.email}")
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
