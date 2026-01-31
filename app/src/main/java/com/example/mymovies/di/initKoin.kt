package com.example.mymovies.di

import com.example.movie.presentation.di.moviesModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            moviesModule,
        )
    }
}