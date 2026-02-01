package com.example.movie.presentation.movie_list.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.movie.presentation.model.MovieUi
@Composable
fun MoviesList(
    movies: List<MovieUi>,
    isPaginationLoading: Boolean,
    paginationError: String?,
    scrollState: LazyGridState,
    onMovieClick: (MovieUi) -> Unit,
    onRetryPaginationClick: () -> Unit,
    onLoadMore: () -> Unit, // ✅ 1. تمت الإضافة هنا
    modifier: Modifier = Modifier
) {
    // ✅ 2. استدعاء الـ Listener عشان يراقب السكرول
    PaginationScrollListener(
        gridState = scrollState,
        itemCount = movies.size,
        isPaginationLoading = isPaginationLoading,
        isEndReached = false, // ممكن تمررها كباراميتر لو حابب، أو تسيبها false والـ VM يهندلها
        onLoadMore = onLoadMore
    )

    if (movies.isEmpty() && !isPaginationLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No movies found")
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = scrollState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            items(
                items = movies,
                key = { it.id }
            ) { movie ->
                MovieListItem(
                    movie = movie,
                    onMovieClick = onMovieClick
                )
            }

            if (isPaginationLoading || paginationError != null) {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    if (isPaginationLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (paginationError != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = paginationError, color = MaterialTheme.colorScheme.error)
                            Button(onClick = onRetryPaginationClick) { Text("Retry") }
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
        Box(modifier = Modifier.fillMaxSize()) {
            MoviesList(
                movies = dummyMovies,
                isPaginationLoading = false,
                paginationError = null,
                scrollState = rememberLazyGridState(),
                onMovieClick = {},
                onRetryPaginationClick = {},
                onLoadMore = {}
            )
        }
    }
}

@Preview(name = "Pagination Loading")
@Composable
private fun MoviesListLoadingPreview() {
    MyMoviesTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            MoviesList(
                movies = dummyMovies,
                isPaginationLoading = true,
                paginationError = null,
                scrollState = rememberLazyGridState(),
                onMovieClick = {},
                onRetryPaginationClick = {},
                onLoadMore = {}
            )
        }
    }
}

@Preview(name = "Pagination Error")
@Composable
private fun MoviesListErrorPreview() {
    MyMoviesTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            MoviesList(
                movies = dummyMovies,
                isPaginationLoading = false,
                paginationError = "Failed to load more movies",
                scrollState = rememberLazyGridState(),
                onMovieClick = {},
                onRetryPaginationClick = {},
                onLoadMore = {}
            )
        }
    }
}