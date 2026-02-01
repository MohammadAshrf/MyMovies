package com.example.movie.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

class DatabaseFactory(
    private val context: Context
) {
    fun create(): RoomDatabase.Builder<MyMoviesDatabase> {
        return Room.databaseBuilder(
            context,
            MyMoviesDatabase::class.java,
            MyMoviesDatabase.DB_NAME
        )
    }
}