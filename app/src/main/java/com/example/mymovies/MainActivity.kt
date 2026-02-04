package com.example.mymovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.movie.presentation.movie_list.MovieListRoot
import com.example.mymovies.navigation.NavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMoviesTheme {
                NavigationRoot()
            }
        }
    }
}