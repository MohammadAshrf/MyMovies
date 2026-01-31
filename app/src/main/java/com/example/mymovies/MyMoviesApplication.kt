package com.example.mymovies

import android.app.Application
import com.example.mymovies.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyMoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MyMoviesApplication)
        }
    }
}