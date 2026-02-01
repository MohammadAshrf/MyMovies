package com.example.movie.presentation.di

import com.example.movie.presentation.movie_list.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val moviesPresentationModule = module {
    viewModelOf(::MovieListViewModel)
}