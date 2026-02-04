@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movie.presentation.movie_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.core.presentation.designsystem.LogoIcon
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.core.presentation.designsystem.components.MoviesScaffold
import com.example.core.presentation.designsystem.components.MoviesToolbar
import com.example.core.presentation.designsystem.toolbar.ToolbarNavIcon
import com.example.core.presentation.ui.util.ObserveAsEvents
import com.example.movie.domain.model.MovieSource
import com.example.movie.presentation.R
import com.example.movie.presentation.model.MovieUi
import com.example.movie.presentation.movie_list.components.MoviesList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieListRoot(
    onMovieClick: (Int, MovieSource) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: MovieListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val movies = viewModel.moviePagingFlow.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        snapshotFlow { movies.loadState }
            .distinctUntilChanged()
            .collect { loadState ->
                val refreshError = loadState.refresh as? LoadState.Error
                val appendError = loadState.append as? LoadState.Error

                refreshError?.takeIf { movies.itemCount > 0 }?.let {
                    viewModel.onPagingError(it.error)
                }

                appendError?.let {
                    viewModel.onPagingError(it.error)
                }
            }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MovieListEvent.OnMovieClick -> onMovieClick(event.movieId, event.source)
            MovieListEvent.OnSearchClick -> onSearchClick()
            is MovieListEvent.OnError -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = event.error.asString(context),
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    MovieListScreen(
        state = state,
        movies = movies,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
fun MovieListScreen(
    state: MovieListState,
    movies: LazyPagingItems<MovieUi>,
    snackbarHostState: SnackbarHostState,
    onAction: (MovieListAction) -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = topAppBarState)
    val gridScrollState = rememberLazyGridState()

    MoviesScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBar = {
            MoviesToolbar(
                isSearchActive = state.isSearchActive,
                searchQuery = state.searchQuery,
                onSearchQueryChange = { onAction(MovieListAction.OnSearchQueryChange(it)) },
                onToggleSearch = { onAction(MovieListAction.OnToggleSearchClick) },
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
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.imePadding()
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MoviesList(
                movies = movies,
                scrollState = gridScrollState,
                onMovieClick = { movie ->
                    onAction(MovieListAction.OnMovieClick(movie))
                },
                onRetry = { movies.retry() },
                modifier = Modifier.fillMaxSize().testTag("movie_list")
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    MyMoviesTheme {
        val dummyMovies = flowOf(
            PagingData.from(
                List(6) {
                    MovieUi(it, "Movie Preview $it", null, "2023", "8.5")
                }
            )
        ).collectAsLazyPagingItems()

        MovieListScreen(
            state = MovieListState(),
            movies = dummyMovies,
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}