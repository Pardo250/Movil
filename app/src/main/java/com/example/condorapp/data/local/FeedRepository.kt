package com.example.condorapp.data.local

import com.example.condorapp.data.FeedPlace

object FeedRepository {
    fun getPlaces(): List<FeedPlace> =
            listOf(
                    FeedPlace("https://picsum.photos/300/300?random=1", "Cartagena"),
                    FeedPlace("https://picsum.photos/300/300?random=2", "Valle del Cocora"),
                    FeedPlace("https://picsum.photos/300/300?random=3", "Santa Marta"),
                    FeedPlace("https://picsum.photos/300/300?random=4", "Medellín"),
                    FeedPlace("https://picsum.photos/300/300?random=5", "Atardecer"),
                    FeedPlace("https://picsum.photos/300/300?random=6", "Catedral")
            )
}
