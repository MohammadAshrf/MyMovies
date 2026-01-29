package com.example.movie.presentation.movie_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.presentation.designsystem.MyMoviesTheme

@Composable
fun MovieListRoot(
    viewModel: MovieListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MovieListScreen(
    state: MovieListState,
    onAction: (MovieListAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    MyMoviesTheme {
        MovieListScreen(
            state = MovieListState(),
            onAction = {}
        )
    }
}