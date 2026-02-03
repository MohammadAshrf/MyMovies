package com.example.movie.presentation.movie_list.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.core.presentation.ui.util.asUiText
import com.example.movie.presentation.R
import com.example.movie.presentation.model.MovieUi
import kotlinx.coroutines.flow.flowOf

@Composable
fun MoviesList(
    movies: LazyPagingItems<MovieUi>,
    scrollState: LazyGridState,
    onMovieClick: (MovieUi) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            keyboardController?.hide()
        }
    }

    val refreshState = movies.loadState.refresh
    val appendState = movies.loadState.append

    when (refreshState) {
        is LoadState.Loading if movies.itemCount == 0 -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = modifier.fillMaxSize()
            ) {
                items(10) {
                    MovieItemShimmer()
                }
            }
        }

        is LoadState.NotLoading if movies.itemCount == 0 && appendState.endOfPaginationReached -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_brain),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .alpha(0.5f),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.no_movies_found),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        is LoadState.Error if movies.itemCount == 0 -> {
            val error = refreshState.error

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.network_error),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = error.asUiText().asString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onRetry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = scrollState,
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = modifier
            ) {

                items(
                    count = movies.itemCount,
                ) { index ->
                    val movie = movies[index]
                    if (movie != null) {
                        MovieListItem(
                            movie = movie,
                            onMovieClick = onMovieClick
                        )
                    }
                }

                if (appendState is LoadState.Loading) {
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (appendState is LoadState.Error) {
                    val error = appendState.error
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = error.asUiText().asString(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Button(
                                onClick = { movies.retry() },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                }
            }
        }
    }
}

private val dummyMovies = List(6) { index ->
    MovieUi(
        id = index,
        title = "Movie Title ${index + 1}",
        posterUrl = null,
        releaseYear = "202${index}",
        rating = "8.5",
    )
}

@Preview(
    name = "Light Mode - Content",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Preview(
    name = "Dark Mode - Content",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF000000
)
@Composable
private fun MoviesListContentPreview() {
    MyMoviesTheme {
        val pagedData = flowOf(PagingData.from(dummyMovies)).collectAsLazyPagingItems()

        Box(modifier = Modifier.fillMaxSize()) {
            MoviesList(
                movies = pagedData,
                scrollState = rememberLazyGridState(),
                onMovieClick = {},
                onRetry = {}
            )
        }
    }
}