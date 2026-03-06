package com.example.condorapp.data.local

import com.example.condorapp.data.Review

object ReviewRepository {
    fun getReviews(): List<Review> = listOf(
        Review("1", "Maria Valen", 5, "Category • $$ • 1.2 miles away\nSupporting line text lorem ipsum...", 12),
        Review("2", "Juan Perez", 4, "Un lugar increíble para visitar en familia.", 5),
        Review("3", "Sofia Gomez", 5, "Las mejores vistas de Colombia.", 8)
    )
}
