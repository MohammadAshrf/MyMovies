package com.example.movie.presentation.movie_list

import com.example.core.presentation.ui.util.UiText
import com.example.movie.domain.model.MovieSource

sealed interface MovieListEvent {
    data class OnMovieClick(val movieId: Int, val source: MovieSource) : MovieListEvent
    data object OnSearchClick : MovieListEvent
    data class OnError(val error: UiText) : MovieListEvent
}