package com.example.movie.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.dao.MovieRemoteKeysDao
import com.example.movie.database.entities.MovieEntity
import com.example.movie.database.entities.MovieRemoteKeys

@Database(
    entities = [
        MovieEntity::class,
        MovieRemoteKeys::class
    ],
    version = 1
)
abstract class MyMoviesDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val movieRemoteKeysDao: MovieRemoteKeysDao

    companion object {
        const val DB_NAME = "movies.db"
    }
}