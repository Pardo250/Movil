package com.example.condorapp.ui.editprofile

/**
 * Estado del UI para la pantalla de edición de perfil. Contiene los campos editables y el estado de
 * mensajes de feedback.
 */
data class EditProfileUiState(
        val username: String = "",
        val fullName: String = "",
        val bio: String = "",
        val imageUrl: String? = null,
        val isUploadingImage: Boolean = false,
        val imageUploadError: String? = null,
        val messageRes: Int? = null,
        val isSignedOut: Boolean = false
) {
    /** Habilita el botón de guardar cuando los campos obligatorios tienen contenido. */
    val canSave: Boolean
        get() = username.isNotBlank() && fullName.isNotBlank()
}
