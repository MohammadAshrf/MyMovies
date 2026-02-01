package com.example.movie.data.movie

import com.example.core.data.networking.get
import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.core.domain.util.map
import com.example.movie.data.dto.MovieDto
import com.example.movie.data.dto.response.MoviesResponseDto
import com.example.movie.data.mapper.toDomain
import com.example.movie.domain.model.Movie
import com.example.movie.domain.movie.MovieService
import io.ktor.client.HttpClient

class KtorMovieService(
    private val httpClient: HttpClient
) : MovieService {

    override suspend fun getMovies(page: Int): Result<List<Movie>, DataError.Remote> {
        return httpClient.get<MoviesResponseDto>(
            route = "movie/now_playing",
            queryParams = mapOf(
                "page" to page.toString()
            )
        ).map { responseDto ->
            responseDto.results.map { it.toDomain() }
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Result<List<Movie>, DataError.Remote> {
        return httpClient.get<MoviesResponseDto>(
            route = "search/movie",
            queryParams = mapOf(
                "query" to query,
                "page" to page.toString()
            )
        ).map { responseDto ->
            responseDto.results.map { it.toDomain() }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.Remote> {
        return httpClient.get<MovieDto>(
            route = "movie/$movieId"
        ).map { dto ->
            dto.toDomain()
        }
    }
}