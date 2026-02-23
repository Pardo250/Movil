package com.example.condorapp.data

import com.example.condorapp.R
import com.example.condorapp.ui.Post

object PostRepository {
    fun getPosts(): List<Post> = listOf(
        Post("Alejandra Gomez", "Valle del Cocora", R.drawable.valle_del_cocora, "1.2k", "58"),
        Post("Mateo Ruiz", "Cartagena Old City", R.drawable.cartagena, "980", "30")
    )
}
