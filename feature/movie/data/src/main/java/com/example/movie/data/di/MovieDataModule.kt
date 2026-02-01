package com.example.movie.data.di

import com.example.movie.database.DatabaseFactory
import com.example.movie.database.MyMoviesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val movieDataModule = module {

    single {
       DatabaseFactory(androidContext())
            .create()
            .build()
    }

    single { get<MyMoviesDatabase>().movieDao }
    single { get<MyMoviesDatabase>().movieRemoteKeysDao }

}