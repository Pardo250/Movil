package com.example.condorapp.data.repository

import com.example.condorapp.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser?
        get() = remoteDataSource.getCurrentUser()

    suspend fun signIn(email: String, password: String): AuthResult {
        return remoteDataSource.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String): AuthResult {
        return remoteDataSource.signUp(email, password)
    }
}
