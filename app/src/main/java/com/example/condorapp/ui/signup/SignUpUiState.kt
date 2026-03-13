package com.example.condorapp.ui.signup

/**
 * Estado del UI para la pantalla de registro de usuario. Contiene todos los campos del formulario
 * de registro.
 */
data class SignUpUiState(
        val name: String = "",
        val lastName: String = "",
        val username: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isSignUpSuccessful: Boolean = false,
        val messageRes: Int? = null
) {
    /** Habilita el botón de registro cuando todos los campos son válidos. */
    val canSignUp: Boolean
        get() =
                name.isNotBlank() &&
                        lastName.isNotBlank() &&
                        username.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        password == confirmPassword
}
