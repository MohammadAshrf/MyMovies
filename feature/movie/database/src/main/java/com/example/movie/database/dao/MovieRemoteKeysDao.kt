package com.example.movie.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.movie.database.entities.MovieRemoteKeys

@Dao
interface MovieRemoteKeysDao {

    @Upsert
    suspend fun upsertRemoteKeys(keys: List<MovieRemoteKeys>)

    @Query("SELECT * FROM movie_remote_keys WHERE movieId = :id")
    suspend fun getRemoteKeys(id: Int): MovieRemoteKeys?

    @Query("DELETE FROM movie_remote_keys")
    suspend fun clearRemoteKeys()
}