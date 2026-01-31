package com.example.mymovies.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.movie.presentation.navigation.MovieGraphRoutes
import com.example.movie.presentation.navigation.moviesGraph

@Composable
fun NavigationRoot(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = MovieGraphRoutes.Graph
    ) {
        moviesGraph(navController)
    }
}