package com.example.movie.presentation.navigation

import com.example.movie.domain.model.MovieSource
import kotlinx.serialization.Serializable

sealed interface MovieGraphRoutes {
    @Serializable
    data object Graph : MovieGraphRoutes

    @Serializable
    data object MovieList : MovieGraphRoutes

    @Serializable
    data class MovieDetail(val movieId: Int, val source: MovieSource) : MovieGraphRoutes

    @Serializable
    data object Search : MovieGraphRoutes
}