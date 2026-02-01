package com.example.movie.presentation.mapper

import com.example.core.presentation.ui.BuildConfig
import com.example.movie.domain.model.Movie
import com.example.movie.presentation.model.MovieUi

fun Movie.toUi(): MovieUi {
    val year = releaseDate?.take(4) ?: "—"
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