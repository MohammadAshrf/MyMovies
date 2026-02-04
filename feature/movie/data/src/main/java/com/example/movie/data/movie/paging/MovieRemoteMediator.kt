@file:OptIn(ExperimentalPagingApi::class)

package com.example.movie.data.movie.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.core.domain.util.DataErrorException
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.movie.data.mapper.toEntity
import com.example.movie.database.MyMoviesDatabase
import com.example.movie.database.entities.MovieEntity
import com.example.movie.database.entities.MovieRemoteKeys
import com.example.movie.domain.movie.MovieService

class MovieRemoteMediator(
    private val db: MyMoviesDatabase,
    private val service: MovieService,
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1

            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                if (remoteKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }

                val nextKey = remoteKey.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                nextKey
            }
        }

        return try {
            var endReached = false

            service.getMovies(page)
                .onSuccess { response ->
                    val movies = response.movies
                    endReached = response.page >= response.totalPages

                    db.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            db.movieRemoteKeysDao.clearRemoteKeys()
                            db.movieDao.clearAllMovies()
                        }

                        val prevKey = if (page == 1) null else page - 1
                        val nextKey = if (endReached) null else page + 1

                        val keys = movies.map {
                            MovieRemoteKeys(
                                movieId = it.id,
                                prevKey = prevKey,
                                nextKey = nextKey
                            )
                        }

                        val startingIndex = (page - 1) * 10000

                        val movieEntities = movies.mapIndexed { index, movieDto ->
                            movieDto.toEntity(orderIndex = startingIndex + index)
                        }

                        db.movieRemoteKeysDao.upsertRemoteKeys(keys)
                        db.movieDao.insertMovies(movieEntities)
                    }

                }
                .onFailure { error ->
                    throw DataErrorException(error)
                }

            MediatorResult.Success(endOfPaginationReached = endReached)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data
            ?.lastOrNull()
            ?.let { movie ->
                db.movieRemoteKeysDao.getRemoteKeys(movie.id)
            }
    }
}