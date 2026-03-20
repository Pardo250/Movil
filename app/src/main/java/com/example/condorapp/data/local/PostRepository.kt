package com.example.condorapp.data.local

import com.example.condorapp.data.Post

object PostRepository {
    fun getPosts(): List<Post> =
            listOf(
                    Post(
                            "Alejandra Gomez",
                            "Valle del Cocora",
                            "https://picsum.photos/400/300?random=1",
                            "1.2k",
                            "58"
                    ),
                    Post(
                            "Mateo Ruiz",
                            "Cartagena Old City",
                            "https://picsum.photos/400/300?random=2",
                            "980",
                            "30"
                    ),
                    Post(
                            "Sofia Lopez",
                            "Santa Marta",
                            "https://picsum.photos/400/300?random=3",
                            "750",
                            "42"
                    ),
                    Post(
                            "Carlos Perez",
                            "Medellín",
                            "https://picsum.photos/400/300?random=4",
                            "1.5k",
                            "65"
                    )
            )
}
