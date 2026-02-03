package com.example.movie.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.movie.database.entities.MovieEntity

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Query("""
    SELECT movie_entity.*
    FROM movie_entity
    INNER JOIN movie_remote_keys 
        ON movie_entity.id = movie_remote_keys.movieId
    ORDER BY 
        COALESCE(movie_remote_keys.prevKey, 0) ASC,
        movie_entity.id ASC
""")
    fun getMoviesPagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movie_entity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Query("DELETE FROM movie_entity")
    suspend fun clearAllMovies()
}