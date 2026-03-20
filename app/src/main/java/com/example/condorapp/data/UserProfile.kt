package com.example.condorapp.data

data class UserProfile(
    val name: String,
    val username: String,
    val avatarUrl: String,
    val photos: List<String> = emptyList()
)
