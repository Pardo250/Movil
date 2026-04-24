package com.example.condorapp.data.repository

import com.example.condorapp.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val messaging: FirebaseMessaging,
    private val firestore: FirebaseFirestore
) {
    val currentUser: FirebaseUser?
        get() = remoteDataSource.getCurrentUser()

    suspend fun signIn(email: String, password: String): AuthResult {
        return remoteDataSource.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String): AuthResult {
        return remoteDataSource.signUp(email, password)
    }

    fun signOut() {
        remoteDataSource.signOut()
    }

    /**
     * Obtiene el token FCM actual y lo guarda en el documento de Firestore del usuario.
     * Debería llamarse justo después de un login o registro exitoso.
     */
    suspend fun saveFcmToken() {
        try {
            val user = currentUser ?: return
            val token = messaging.token.await()
            firestore.collection("users").document(user.uid).update("fcmToken", token).await()
        } catch (e: Exception) {
            // Ignorar errores silenciosamente, el token no es crítico para el uso básico
            e.printStackTrace()
        }
    }
}
