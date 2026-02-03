package com.example.movie.presentation.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.movie.presentation.movie_list.MovieListRoot

fun NavGraphBuilder.moviesGraph(
    navController: NavController
) {
    navigation<MovieGraphRoutes.Graph>(
        startDestination = MovieGraphRoutes.MovieList
    ) {
        composable<MovieGraphRoutes.MovieList> {
            MovieListRoot(
                onMovieClick = { movieId ->
                    navController.navigate(MovieGraphRoutes.MovieDetail(movieId))
                },
                onSearchClick = {
                    navController.navigate(MovieGraphRoutes.Search)
                }
            )
        }
    }
}