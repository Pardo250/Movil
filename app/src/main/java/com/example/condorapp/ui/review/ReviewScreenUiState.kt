package com.example.condorapp.ui.review

import com.example.condorapp.data.Review

data class ReviewScreenUiState(
        val review: Review? = null,
        val comments: List<Review> = emptyList(),
        val userComment: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
)
