package com.example.movie.domain.movie

import androidx.paging.PagingData
import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.movie.domain.model.Movie
import com.example.movie.domain.model.MovieSource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(): Flow<PagingData<Movie>>

    fun getMovieDetails(movieId: Int, source: MovieSource): Flow<Result<Movie, DataError.Remote>>

    fun searchMovies(query: String): Flow<PagingData<Movie>>
}