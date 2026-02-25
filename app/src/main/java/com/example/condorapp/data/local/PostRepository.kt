package com.example.condorapp.data.local

import com.example.condorapp.R
import com.example.condorapp.data.Post

object PostRepository {
    fun getPosts(): List<Post> = listOf(
        Post("Alejandra Gomez", "Valle del Cocora", R.drawable.valle_del_cocora, "1.2k", "58"),
        Post("Mateo Ruiz", "Cartagena Old City", R.drawable.cartagena, "980", "30"),
        Post("Sofia Lopez", "Santa Marta", R.drawable.santamarta, "750", "42"),
        Post("Carlos Perez", "Medellín", R.drawable.medellin, "1.5k", "65")
    )
}
