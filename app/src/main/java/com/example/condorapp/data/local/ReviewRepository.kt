package com.example.condorapp.data.local

import com.example.condorapp.data.Review

object ReviewRepository {
    fun getReviews(): List<Review> = listOf(
        Review("Maria Valen", 5, "Category • $$ • 1.2 miles away\nSupporting line text lorem ipsum...", 12),
        Review("Juan Perez", 4, "Un lugar increíble para visitar en familia.", 5),
        Review("Sofia Gomez", 5, "Las mejores vistas de Colombia.", 8)
    )
}
