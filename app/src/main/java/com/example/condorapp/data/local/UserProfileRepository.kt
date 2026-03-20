package com.example.condorapp.data.local

import com.example.condorapp.data.UserProfile

object UserProfileRepository {
    fun getProfile(): UserProfile =
            UserProfile(
                    name = "Camilo Jiménez",
                    username = "@Camilo_co",
                    avatarUrl = "https://i.pravatar.cc/150?img=5",
                    photos =
                            listOf(
                                    "https://picsum.photos/300/300?random=1",
                                    "https://picsum.photos/300/300?random=2",
                                    "https://picsum.photos/300/300?random=3",
                                    "https://picsum.photos/300/300?random=4",
                                    "https://picsum.photos/300/300?random=5",
                                    "https://picsum.photos/300/300?random=6"
                            )
            )
}
