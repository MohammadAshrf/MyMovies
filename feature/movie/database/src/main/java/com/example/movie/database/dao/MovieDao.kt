package com.example.movie.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.movie.database.entities.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie_entity ORDER BY orderIndex ASC")
    fun getMoviesPagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movie_entity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Query("DELETE FROM movie_entity")
    suspend fun clearAllMovies()
}