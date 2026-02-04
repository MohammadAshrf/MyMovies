package com.example.movie.presentation.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.movie.presentation.movie_detail.MovieDetailRoot
import com.example.movie.presentation.movie_list.MovieListRoot

fun NavGraphBuilder.moviesGraph(
    navController: NavController
) {
    navigation<MovieGraphRoutes.Graph>(
        startDestination = MovieGraphRoutes.MovieList
    ) {
        composable<MovieGraphRoutes.MovieList> {
            MovieListRoot(
                onMovieClick = { movieId, source ->
                    navController.navigate(MovieGraphRoutes.MovieDetail(movieId, source))
                },
                onSearchClick = {
                    navController.navigate(MovieGraphRoutes.Search)
                }
            )
        }

        composable<MovieGraphRoutes.MovieDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<MovieGraphRoutes.MovieDetail>()

            MovieDetailRoot(
                movieId = args.movieId,
                source = args.source,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}