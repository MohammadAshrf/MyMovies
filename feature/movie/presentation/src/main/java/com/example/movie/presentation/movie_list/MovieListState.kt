package com.example.movie.presentation.movie_list

data class MovieListState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)