package com.example.condorapp.data.local

import com.example.condorapp.R
import com.example.condorapp.data.FeedPlace

object FeedRepository {
    fun getPlaces(): List<FeedPlace> = listOf(
        FeedPlace("Cartagena", R.drawable.cartagena),
        FeedPlace("Valle del Cocora", R.drawable.valle_del_cocora),
        FeedPlace("Santa Marta", R.drawable.santamarta),
        FeedPlace("Medellín", R.drawable.medellin),
        FeedPlace("Atardecer", R.drawable.atardecer),
        FeedPlace("Catedral", R.drawable.catedral)
    )
}
