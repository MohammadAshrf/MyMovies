package com.example.mymovies.di

import com.example.movie.data.di.movieDataModule
import com.example.movie.presentation.di.moviesPresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            moviesPresentationModule,
            movieDataModule
        )
    }
}