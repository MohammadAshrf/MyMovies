package com.example.movie.domain.model

data class PagedMovies(
    val page: Int,
    val totalPages: Int,
    val movies: List<Movie>
)