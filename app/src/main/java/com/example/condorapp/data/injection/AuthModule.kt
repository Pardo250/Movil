package com.example.condorapp.data.injection

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        val auth = FirebaseAuth.getInstance()
        if (com.example.condorapp.BuildConfig.DEBUG) {
            try {
                auth.useEmulator("10.0.2.2", 9099)
            } catch (e: Exception) {
                // Ignorar si ya fue configurado
            }
        }
        return auth
    }
}
