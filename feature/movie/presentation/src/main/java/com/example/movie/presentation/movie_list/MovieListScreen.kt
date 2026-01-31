@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movie.presentation.movie_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.presentation.designsystem.LogoIcon
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.core.presentation.designsystem.components.MoviesScaffold
import com.example.core.presentation.designsystem.components.MoviesToolbar
import com.example.core.presentation.designsystem.toolbar.ToolbarNavIcon
import com.example.core.presentation.ui.util.ObserveAsEvents
import com.example.movie.presentation.R
import com.example.movie.presentation.model.MovieUi
import com.example.movie.presentation.movie_list.components.MoviesList
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieListRoot(
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: MovieListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MovieListEvent.OnMovieClick -> onMovieClick(event.movieId)
            MovieListEvent.OnSearchClick -> onSearchClick()
            is MovieListEvent.OnError -> {
                // Handle Error (Snackbar)
            }
        }
    }

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
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = topAppBarState)
    val gridScrollState = rememberLazyGridState()

    MoviesScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBar = {
            MoviesToolbar(
                navIcons = setOf(ToolbarNavIcon.SEARCH),
                title = stringResource(R.string.movies),
                scrollBehavior = scrollBehavior,
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onSearchClick = {
                    onAction(MovieListAction.OnSearchClick)
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                // 1. Loading Initial
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // 2. Error Initial (Empty List)
                state.error != null && state.movies.isEmpty() -> {
                    Text(
                        text = state.error.asString(),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                    // يفضل إضافة زرار Retry هنا
                }

                // 3. Success (List)
                else -> {
                    MoviesList(
                        movies = state.movies,
                        isPaginationLoading = state.isPaginationLoading,
                        paginationError = state.paginationError?.asString(),
                        scrollState = gridScrollState,
                        onMovieClick = { movie ->
                            onAction(MovieListAction.OnMovieClick(movie))
                        },
                        onRetryPaginationClick = {
                            onAction(MovieListAction.OnRetryPaginationClick)
                        },
                        // ✅ الربط المهم جداً مع الـ ViewModel
                        onLoadMore = {
                            onAction(MovieListAction.OnLoadMore)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    MyMoviesTheme {
        MovieListScreen(
            state = MovieListState(
                movies = List(6) {
                    MovieUi(it, "Movie Preview $it", null, "2023", 8.5)
                }
            ),
            onAction = {}
        )
    }
}