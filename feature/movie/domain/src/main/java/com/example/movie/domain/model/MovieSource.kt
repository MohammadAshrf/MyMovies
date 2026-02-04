package com.example.movie.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MovieSource {
    LIST,
    SEARCH
}