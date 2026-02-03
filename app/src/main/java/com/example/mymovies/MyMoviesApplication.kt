package com.example.mymovies

import android.app.Application
import com.example.mymovies.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import timber.log.Timber

class MyMoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initKoin {
            androidLogger()
            androidContext(this@MyMoviesApplication)
        }
    }
}