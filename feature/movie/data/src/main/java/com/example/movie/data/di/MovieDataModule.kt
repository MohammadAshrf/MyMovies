package com.example.movie.data.di

import com.example.movie.data.chat.KtorMovieService
import com.example.movie.database.DatabaseFactory
import com.example.movie.database.MyMoviesDatabase
import com.example.movie.domain.movie.MovieService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val movieDataModule = module {

    single {
        DatabaseFactory(androidContext())
            .create()
            .build()
    }

    single { get<MyMoviesDatabase>().movieDao }
    single { get<MyMoviesDatabase>().movieRemoteKeysDao }
    singleOf(::KtorMovieService).bind<MovieService>()
}