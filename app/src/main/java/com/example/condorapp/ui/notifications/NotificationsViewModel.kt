package com.example.condorapp.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condorapp.data.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.tasks.await

/**
 * ViewModel para la pantalla de notificaciones.
 * Lee en tiempo real la subcolección notifications/{uid}/items de Firestore.
 *
 * Las notificaciones se crean desde:
 *   - Cloud Function sendLikeNotification (cuando alguien da like)
 *   - UsuarioFirestoreDataSource.toggleFollow() (cuando alguien te sigue)
 */
@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        listenNotifications()
    }

    /** Escucha en tiempo real las notificaciones del usuario actual en Firestore. */
    private fun listenNotifications() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            callbackFlow<List<Notification>> {
                val listener = db.collection("notifications")
                    .document(uid)
                    .collection("items")
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(50)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null || snapshot == null) {
                            trySend(emptyList())
                            return@addSnapshotListener
                        }
                        val list = snapshot.documents.mapNotNull { doc ->
                            try {
                                Notification(
                                    id        = doc.id,
                                    userName  = doc.getString("userName") ?: "",
                                    action    = doc.getString("action") ?: "",
                                    time      = doc.getString("time") ?: "",
                                    avatarUrl = doc.getString("avatarUrl") ?: "",
                                    type      = doc.getString("type") ?: "like",
                                    createdAt = doc.getLong("createdAt") ?: 0L
                                )
                            } catch (e: Exception) { null }
                        }
                        trySend(list)
                    }
                awaitClose { listener.remove() }
            }.collect { notifications ->
                _uiState.update { it.copy(notifications = notifications, isLoading = false) }
            }
        }
    }

    /** Cambia el tab seleccionado (Todos / Likes / Seguidores). */
    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    /** Limpia todas las notificaciones del usuario en Firestore. */
    fun onClearAll() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(notifications = emptyList()) }
            // Eliminar en Firestore (sin bloquear la UI)
            try {
                val items = db.collection("notifications").document(uid)
                    .collection("items").get().await()
                val batch = db.batch()
                items.documents.forEach { batch.delete(it.reference) }
                batch.commit()
            } catch (_: Exception) {}
        }
    }
}

