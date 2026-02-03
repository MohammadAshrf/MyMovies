package com.example.movie.presentation.model

data class MovieDetailUi(
    val id: Int,
    val title: String,
    val overview: String?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String?,
    val rating: String,
    val ratingCount: String?
)