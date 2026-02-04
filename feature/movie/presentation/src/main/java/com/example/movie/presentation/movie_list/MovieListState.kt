package com.example.movie.presentation.movie_list

data class MovieListState(
    val isSearchActive: Boolean = false,
    val searchQuery: String = ""
)