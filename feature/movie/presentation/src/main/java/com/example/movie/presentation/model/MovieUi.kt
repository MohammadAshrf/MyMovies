package com.example.movie.presentation.model

data class MovieUi(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val releaseYear: String,
    val rating: Double
)