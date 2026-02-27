package com.example.condorapp.data.local

import com.example.condorapp.R
import com.example.condorapp.data.FeedPlace

object FeedRepository {
    fun getPlaces(): List<FeedPlace> = listOf(
        FeedPlace(R.drawable.cartagena, "Cartagena"),
        FeedPlace(R.drawable.valle_del_cocora, "Valle del Cocora"),
        FeedPlace(R.drawable.santamarta, "Santa Marta"),
        FeedPlace(R.drawable.medellin, "Medellín"),
        FeedPlace(R.drawable.atardecer, "Atardecer"),
        FeedPlace(R.drawable.catedral, "Catedral")
    )
}
