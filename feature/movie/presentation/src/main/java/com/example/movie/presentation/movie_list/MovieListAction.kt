package com.example.movie.presentation.movie_list

import com.example.movie.presentation.model.MovieUi

sealed interface MovieListAction {
    data object OnToggleSearchClick : MovieListAction
    data class OnSearchQueryChange(val query: String) : MovieListAction
    data class OnMovieClick(val movie: MovieUi) : MovieListAction
}