package com.example.movie.presentation.movie_list

import com.example.core.presentation.ui.util.UiText
import com.example.movie.presentation.model.MovieUi

data class MovieListState(
    val movies: List<MovieUi> = emptyList(),
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val error: UiText? = null,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val page: Int = 1,
)