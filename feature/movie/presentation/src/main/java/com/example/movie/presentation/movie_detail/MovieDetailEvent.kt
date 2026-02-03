package com.example.movie.presentation.movie_detail

import com.example.core.presentation.ui.util.UiText

sealed interface MovieDetailEvent {
    data class OnError(val error: UiText) : MovieDetailEvent
    data object OnBack : MovieDetailEvent
}