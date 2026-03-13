package com.example.condorapp.ui.login

/**
 * Estado del UI para la pantalla de inicio de sesión. Contiene los campos del formulario y el
 * estado de mensajes.
 */
data class LoginUiState(
        val email: String = "",
        val password: String = "",
        val showPassword: Boolean = false,
        val messageRes: Int? = null,
        val isLoginSuccessful: Boolean = false
) {
    /** Habilita el botón de Sign In cuando ambos campos tienen contenido. */
    val canSignIn: Boolean
        get() = email.isNotBlank() && password.isNotBlank()
}
