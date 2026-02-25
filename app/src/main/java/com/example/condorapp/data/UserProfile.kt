package com.example.condorapp.data

data class UserProfile(
    val name: String,
    val username: String,
    val avatarRes: Int,
    val photos: List<Int> = emptyList()
)
