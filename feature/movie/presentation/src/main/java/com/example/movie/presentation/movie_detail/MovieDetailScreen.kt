@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movie.presentation.movie_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.core.presentation.designsystem.components.MoviesToolbar
import com.example.core.presentation.designsystem.toolbar.ToolbarNavIcon
import com.example.core.presentation.ui.util.ObserveAsEvents
import com.example.movie.presentation.R
import com.example.movie.presentation.model.MovieDetailUi
import com.example.movie.presentation.movie_detail.components.MovieDetailShimmer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailRoot(
    movieId: Int,
    onBackClick: () -> Unit,
    viewModel: MovieDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            MovieDetailEvent.OnBack -> onBackClick()
            is MovieDetailEvent.OnError -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = event.error.asString(context),
                    withDismissAction = true
                )
            }
        }
    }

    LaunchedEffect(movieId) {
        viewModel.onAction(MovieDetailAction.OnLoadMovie(movieId))
    }

    MovieDetailScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
fun MovieDetailScreen(
    state: MovieDetailState,
    snackbarHostState: SnackbarHostState,
    onAction: (MovieDetailAction) -> Unit
) {
    Scaffold(
        topBar = {
            MoviesToolbar(
                title = stringResource(R.string.movie_details),
                navIcons = setOf(ToolbarNavIcon.BACK),
                onBackClick = { onAction(MovieDetailAction.OnBackClick) }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading && state.movie == null) {
                MovieDetailShimmer(
                    modifier = Modifier.fillMaxSize()
                )
            }

            state.movie?.let { movie ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.backdropUrl ?: movie.posterUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = movie.title,
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.empty_place_holder),
                        error = painterResource(id = R.drawable.empty_place_holder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(
                                R.string.released,
                                movie.releaseDate.orEmpty(),
                                movie.rating,
                                movie.ratingCount.orEmpty()
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.overview),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = movie.overview.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MovieDetailPreview() {
    MyMoviesTheme {
        MovieDetailScreen(
            state = MovieDetailState(
                isLoading = false,
                movie = MovieDetailUi(
                    id = 1,
                    title = "Inception",
                    overview = "A thief who steals corporate secrets through the use of dream-sharing technology...",
                    posterUrl = null,
                    backdropUrl = null,
                    releaseDate = "30-1-2010",
                    rating = "8.8",
                    ratingCount = "(1000)"
                )
            ),
            snackbarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}