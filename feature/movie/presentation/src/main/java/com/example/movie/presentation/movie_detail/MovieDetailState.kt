package com.example.movie.presentation.movie_detail

import com.example.movie.presentation.model.MovieDetailUi

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: MovieDetailUi? = null,
)