package com.example.movie.presentation.movie_detail

sealed interface MovieDetailAction {
    data class OnLoadMovie(val movieId: Int) : MovieDetailAction
    data object OnBackClick : MovieDetailAction
}