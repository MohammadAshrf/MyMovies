package com.example.movie.presentation

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movie.presentation.model.MovieUi
import com.example.movie.presentation.movie_list.MovieListScreen
import com.example.movie.presentation.movie_list.MovieListState
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class MovieListScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun app_displays_list_and_can_scroll() {
        val moviesCount = 20
        val fakeMovies = (1..moviesCount).map { index ->
            MovieUi(
                id = index,
                title = "Movie Title $index",
                posterUrl = null,
                releaseYear = "2026",
                rating = "9.0"
            )
        }

        val fakePagingFlow = flowOf(PagingData.from(fakeMovies))

        composeRule.setContent {
            val movies = fakePagingFlow.collectAsLazyPagingItems()
            val snackbarHostState = remember { SnackbarHostState() }

            MovieListScreen(
                state = MovieListState(isSearchActive = false, searchQuery = ""),
                movies = movies,
                snackbarHostState = snackbarHostState,
                onAction = {}
            )
        }

        composeRule.onNodeWithTag("movie_list")
            .assertExists()

        composeRule.onNodeWithTag("movie_list")
            .performScrollToIndex(moviesCount - 1)

        composeRule.onNodeWithText("Movie Title $moviesCount")
            .assertIsDisplayed()

        Thread.sleep(5000)
    }
}