@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.movie.presentation.movie_list

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.movie.domain.model.Movie
import com.example.movie.domain.model.MovieSource
import com.example.movie.domain.movie.MovieRepository
import com.example.movie.presentation.model.MovieUi
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class MovieListViewModelTest {

    private val repository: MovieRepository = mockk(relaxed = true)
    private lateinit var viewModel: MovieListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when toggle search clicked then search should be enabled and disabled`() = runTest {
        Truth.assertThat(viewModel.state.value.isSearchActive).isFalse()

        viewModel.onAction(MovieListAction.OnToggleSearchClick)
        advanceUntilIdle()

        Truth.assertThat(viewModel.state.value.isSearchActive).isTrue()

        viewModel.onAction(MovieListAction.OnToggleSearchClick)
        advanceUntilIdle()

        Truth.assertThat(viewModel.state.value.isSearchActive).isFalse()
        Truth.assertThat(viewModel.state.value.searchQuery).isEmpty()
    }

    @Test
    fun `when search query changes then state should be updated`() = runTest {
        val query = "batman"

        viewModel.onAction(
            MovieListAction.OnSearchQueryChange(query)
        )
        advanceUntilIdle()

        Truth.assertThat(viewModel.state.value.searchQuery).isEqualTo(query)
    }

    @Test
    fun `when search query is blank then getMovies should be called`() = runTest {
        val pagingData = PagingData.from(emptyList<Movie>())
        every { repository.getMovies() } returns flowOf(pagingData)

        viewModel.moviePagingFlow.test {
            viewModel.onAction(
                MovieListAction.OnSearchQueryChange("")
            )

            advanceTimeBy(300)

            awaitItem()
            verify(exactly = 1) { repository.getMovies() }
        }
    }

    @Test
    fun `when search query is not blank then searchMovies should be called`() = runTest {
        val query = "elio"
        val pagingData = PagingData.from(emptyList<Movie>())
        every { repository.searchMovies(query) } returns flowOf(pagingData)

        viewModel.moviePagingFlow.test {
            viewModel.onAction(
                MovieListAction.OnSearchQueryChange(query)
            )

            advanceTimeBy(300)

            awaitItem()
            verify(exactly = 1) { repository.searchMovies(query) }
        }
    }

    @Test
    fun `when movie clicked from list then navigation event with LIST source should be emitted`() =
        runTest {
            val movieId = 42
            val movieUi = MovieUi(
                id = movieId,
                title = "Batman",
                posterUrl = null,
                releaseYear = null,
                rating = "9.0"
            )

            viewModel.events.test {
                viewModel.onAction(
                    MovieListAction.OnMovieClick(movie = movieUi)
                )

                val event = awaitItem()

                Truth.assertThat(event)
                    .isEqualTo(
                        MovieListEvent.OnMovieClick(
                            movieId = movieId,
                            source = MovieSource.LIST
                        )
                    )

                cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun `when movie clicked from search then navigation event with SEARCH source should be emitted`() =
        runTest {
            val movieId = 99
            val movieUi = MovieUi(
                id = movieId,
                title = "Inception",
                posterUrl = null,
                releaseYear = "2010",
                rating = "8.8"
            )

            viewModel.events.test {
                viewModel.onAction(MovieListAction.OnToggleSearchClick)
                viewModel.onAction(MovieListAction.OnMovieClick(movie = movieUi))

                val event = awaitItem()

                Truth.assertThat(event)
                    .isEqualTo(
                        MovieListEvent.OnMovieClick(
                            movieId = movieId,
                            source = MovieSource.SEARCH
                        )
                    )

                cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun `when paging error occurs then error event should be emitted`() = runTest {
        val error = RuntimeException("Network error")

        viewModel.events.test {
            viewModel.onPagingError(error)

            val event = awaitItem()
            Truth.assertThat(event)
                .isInstanceOf(MovieListEvent.OnError::class.java)
        }
    }
}