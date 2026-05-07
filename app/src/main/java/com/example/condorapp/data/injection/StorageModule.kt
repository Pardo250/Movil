package com.example.condorapp.data.injection

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        val storage = Firebase.storage
        if (com.example.condorapp.BuildConfig.DEBUG) {
            try {
                storage.useEmulator("10.0.2.2", 9199)
            } catch (e: Exception) {
                // Ignorar si ya fue configurado
            }
        }
        return storage
    }
}
