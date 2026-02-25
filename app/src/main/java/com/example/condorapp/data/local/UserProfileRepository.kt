package com.example.condorapp.data.local

import com.example.condorapp.R
import com.example.condorapp.data.UserProfile

object UserProfileRepository {
    fun getProfile(): UserProfile = UserProfile(
        name = "Camilo Jiménez",
        username = "@Camilo_co",
        avatarRes = R.drawable.avatar,
        photos = listOf(
            R.drawable.cartagena,
            R.drawable.valle_del_cocora,
            R.drawable.medellin,
            R.drawable.santamarta,
            R.drawable.catedral,
            R.drawable.atardecer
        )
    )
}
