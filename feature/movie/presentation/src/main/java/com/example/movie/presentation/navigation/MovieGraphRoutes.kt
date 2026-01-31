package com.example.movie.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface MovieGraphRoutes {
    @Serializable
    data object Graph : MovieGraphRoutes

    @Serializable
    data object MovieList : MovieGraphRoutes

    @Serializable
    data class MovieDetail(val movieId: Int) : MovieGraphRoutes

    @Serializable
    data object Search : MovieGraphRoutes
}