package com.example.condorapp.ui.notifications

import com.example.condorapp.data.Notification

/**
 * Estado del UI para la pantalla de notificaciones. Contiene el tab seleccionado y la lista de
 * notificaciones.
 */
data class NotificationsUiState(
        val selectedTab: String = "Todo",
        val notifications: List<Notification> = emptyList()
)
