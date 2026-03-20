package com.example.condorapp.data.repository

import android.net.Uri
import com.example.condorapp.data.datasource.StorageRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storageRemoteDataSource: StorageRemoteDataSource,
    private val auth: FirebaseAuth
) {
    suspend fun uploadProfileImage(uri: Uri): Result<String> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("Usuario no autenticado"))
            
            val userId = user.uid
            val path = "profile_images/$userId.jpg"
            
            val downloadUrl = storageRemoteDataSource.uploadImage(path, uri)
            
            // Optimización: Actualizamos el UserProfile con la nueva photoUrl
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(downloadUrl))
                .build()
                
            user.updateProfile(profileUpdates).await()
            
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
