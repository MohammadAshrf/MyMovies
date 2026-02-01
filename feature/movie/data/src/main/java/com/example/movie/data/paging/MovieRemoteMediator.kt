@file:OptIn(ExperimentalPagingApi::class)

package com.example.movie.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.core.domain.util.Result
import com.example.movie.data.mapper.toEntity
import com.example.movie.database.MyMoviesDatabase
import com.example.movie.database.entities.MovieEntity
import com.example.movie.database.entities.MovieRemoteKeys
import com.example.movie.domain.movie.MovieService
import java.io.IOException

class MovieRemoteMediator(
    private val db: MyMoviesDatabase,
    private val service: MovieService
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextKey
                }
            }

            when (val result = service.getMovies(page = loadKey)) {
                is Result.Success -> {
                    val movies = result.data
                    val endOfPaginationReached = movies.isEmpty()

                    db.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            db.movieRemoteKeysDao.clearRemoteKeys()
                            db.movieDao.clearAllMovies()
                        }

                        val prevKey = if (loadKey == 1) null else loadKey - 1
                        val nextKey = if (endOfPaginationReached) null else loadKey + 1

                        val keys = movies.map { movie ->
                            MovieRemoteKeys(
                                movieId = movie.id,
                                prevKey = prevKey,
                                nextKey = nextKey,
                            )
                        }

                        db.movieRemoteKeysDao.upsertRemoteKeys(keys)
                        db.movieDao.upsertMovies(movies.map { it.toEntity() })
                    }

                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }

                is Result.Failure -> {
                    MediatorResult.Error(IOException(result.error.name))
                }
            }

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): MovieRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                db.movieRemoteKeysDao.getRemoteKeys(id = movie.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): MovieRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.movieRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }
}