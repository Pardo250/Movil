package com.example.condorapp.data.datasource

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRemoteDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadImage(path: String, uri: Uri): String {
        val reference = storage.reference.child(path)
        reference.putFile(uri).await()
        return reference.downloadUrl.await().toString()
    }
}


