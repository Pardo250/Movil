package com.example.condorapp.ui.notifications

import androidx.lifecycle.ViewModel
import com.example.condorapp.data.local.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de notificaciones. Carga las notificaciones desde el repositorio y
 * gestiona tabs y acciones.
 */
class NotificationsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    /** Carga la lista de notificaciones desde el repositorio local. */
    private fun loadNotifications() {
        val notifications = NotificationRepository.getNotifications()
        _uiState.update { it.copy(notifications = notifications) }
    }

    /** Cambia el tab seleccionado. */
    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    /** Limpia todas las notificaciones. */
    fun onClearAll() {
        _uiState.update { it.copy(notifications = emptyList()) }
    }
}
