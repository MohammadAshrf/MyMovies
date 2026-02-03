@file:OptIn(ExperimentalPagingApi::class)

package com.example.movie.data.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.movie.data.mapper.toDomain
import com.example.movie.data.mapper.toEntity
import com.example.movie.data.movie.paging.MovieRemoteMediator
import com.example.movie.data.movie.paging.SearchPagingSource
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
                prefetchDistance = 4,
                initialLoadSize = 40,
                enablePlaceholders = false,
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

        service.getMovieDetails(movieId)
            .onSuccess {
                val orderIndexToSave = localMovie?.orderIndex ?: Int.MAX_VALUE

                val newEntity = it.toEntity(orderIndexToSave)

                if (localMovie != newEntity) {
                    db.movieDao.upsertMovies(listOf(newEntity))
                }
                emit(Result.Success(it))
            }
            .onFailure { error ->
                if (localMovie == null) {
                    emit(Result.Failure(error))
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