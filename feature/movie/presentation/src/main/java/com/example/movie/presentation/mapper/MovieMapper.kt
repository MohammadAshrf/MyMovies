package com.example.movie.presentation.mapper

import com.example.core.presentation.ui.BuildConfig
import com.example.movie.domain.model.Movie
import com.example.movie.presentation.model.MovieDetailUi
import com.example.movie.presentation.model.MovieUi

fun Movie.toUi(): MovieUi {
    val year = releaseDate?.take(4)?.ifBlank { "—" }
    val rating = if (voteAverage > 0) {
        String.format("%.1f", voteAverage)
    } else {
        "—"
    }
    val fullPosterUrl = posterPath?.let {
        BuildConfig.IMAGE_BASE_URL + it
    }

    return MovieUi(
        id = id,
        title = title,
        posterUrl = fullPosterUrl,
        releaseYear = year,
        rating = rating,
    )
}

fun Movie.toDetailUi(): MovieDetailUi {
    val fullPosterUrl = posterPath?.let {
        BuildConfig.IMAGE_BASE_URL + it
    }
    val fullBackdropUrl = backdropPath?.let {
        BuildConfig.IMAGE_BASE_URL + it
    }

    val rating = if (voteAverage > 0) {
        String.format("%.1f", voteAverage)
    } else {
        "—"
    }

    return MovieDetailUi(
        id = id,
        title = title,
        overview = overview?.ifBlank { "No overview available!" },
        posterUrl = fullPosterUrl,
        backdropUrl = fullBackdropUrl,
        releaseDate = releaseDate?.ifBlank { "—" },
        rating = rating,
        ratingCount = "($voteCount votes)".ifBlank { "—" }
    )
}