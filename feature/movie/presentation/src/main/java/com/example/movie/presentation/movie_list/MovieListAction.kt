package com.example.movie.presentation.movie_list

import com.example.movie.presentation.model.MovieUi

sealed interface MovieListAction {
    data object OnSearchClick : MovieListAction
    data class OnMovieClick(val movie: MovieUi) : MovieListAction
    data object OnLoadMore : MovieListAction
    data object OnRetryClick : MovieListAction
    data object OnRetryPaginationClick : MovieListAction
}