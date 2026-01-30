@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movie.presentation.movie_list

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.presentation.designsystem.LogoIcon
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.core.presentation.designsystem.components.MoviesScaffold
import com.example.core.presentation.designsystem.components.MoviesToolbar
import com.example.core.presentation.designsystem.toolbar.ToolbarNavIcon
import com.example.movie.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieListRoot(
    viewModel: MovieListViewModel = koinViewModel()
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
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )
    MoviesScaffold(
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
                }
            )
        }
    ) {

    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    MyMoviesTheme {
        MovieListScreen(
            state = MovieListState(),
            onAction = {}
        )
    }
}