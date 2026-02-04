package com.example.movie.presentation.movie_detail

import com.example.movie.domain.model.MovieSource

sealed interface MovieDetailAction {
    data class OnLoadMovie(val movieId: Int, val source: MovieSource) : MovieDetailAction
    data object OnBackClick : MovieDetailAction
}