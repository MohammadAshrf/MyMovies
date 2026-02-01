package com.example.movie.domain.movie

import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.movie.domain.model.Movie

interface MovieService {
    suspend fun getMovies(page: Int): Result<List<Movie>, DataError.Remote>
    suspend fun searchMovies(query: String, page: Int): Result<List<Movie>, DataError.Remote>
    suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.Remote>
}