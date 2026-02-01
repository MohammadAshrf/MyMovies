@file:OptIn(ExperimentalPagingApi::class)

package com.example.movie.data.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.movie.data.mapper.toDomain
import com.example.movie.data.mapper.toEntity
import com.example.movie.data.paging.MovieRemoteMediator
import com.example.movie.data.paging.SearchPagingSource
import com.example.movie.database.MyMoviesDatabase
import com.example.movie.domain.model.Movie
import com.example.movie.domain.movie.MovieRepository
import com.example.movie.domain.movie.MovieService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OfflineFirstMovieRepository(
    private val db: MyMoviesDatabase,
    private val service: MovieService
) : MovieRepository {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                initialLoadSize = 40,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(
                db = db,
                service = service
            ),
            pagingSourceFactory = {
                db.movieDao.getMoviesPagingSource()
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }
    }


    override fun getMovieDetails(movieId: Int): Flow<Result<Movie, DataError.Remote>> = flow {
        val localMovie = db.movieDao.getMovieById(movieId)
        if (localMovie != null) {
            emit(Result.Success(localMovie.toDomain()))
        }

        when (val result = service.getMovieDetails(movieId)) {
            is Result.Success -> {
                val remoteMovie = result.data
                db.movieDao.upsertMovies(listOf(remoteMovie.toEntity()))
                emit(Result.Success(remoteMovie))
            }

            is Result.Failure -> {
                if (localMovie == null) {
                    emit(Result.Failure(result.error))
                }
            }
        }
    }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                SearchPagingSource(service, query)
            }
        ).flow.map { pagingData ->
            pagingData.map { it }
        }
    }
}