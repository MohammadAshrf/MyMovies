package com.example.movie.data.mapper

import com.example.movie.data.dto.MovieDto
import com.example.movie.domain.model.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage
    )
}