package com.example.movie.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_remote_keys")
data class MovieRemoteKeys(
    @PrimaryKey
    val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)