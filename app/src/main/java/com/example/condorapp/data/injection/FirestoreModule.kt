package com.example.condorapp.data.injection

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt que provee la instancia singleton de FirebaseFirestore.
 * Equivalente a NetworkModule pero para la base de datos NoSQL.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        if (com.example.condorapp.BuildConfig.DEBUG) {
            try {
                // Descomentar para usar el emulador. Nota: En un dispositivo físico, 
                // "10.0.2.2" NO funcionará, debes poner la IP WiFi de tu PC.
                // firestore.useEmulator("10.0.2.2", 8080)
            } catch (e: Exception) {
                // Ignorar si ya fue configurado
            }
        }
        return firestore
    }
}
