package com.example.movie.presentation.movie_list

import com.example.core.presentation.ui.util.UiText

sealed interface MovieListEvent {
    data class NavigateToDetail(val movieId: Int) : MovieListEvent
    data object NavigateToSearch : MovieListEvent
    data class OnError(val error: UiText) : MovieListEvent
}