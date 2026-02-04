@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.movie.presentation.movie_detail

import app.cash.turbine.test
import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.movie.domain.model.Movie
import com.example.movie.domain.model.MovieSource
import com.example.movie.domain.movie.MovieRepository
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class MovieDetailViewModelTest {

    private val repository: MovieRepository = mockk(relaxed = true)
    private lateinit var viewModel: MovieDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when load movie from list then emits loading then success`() = runTest {
        val movieId = 55
        val source = MovieSource.LIST

        val movie = Movie(
            id = movieId,
            title = "Dune: Part Two",
            overview = "Paul Atreides unites with Chani.",
            posterPath = null,
            backdropPath = null,
            releaseDate = "2024",
            voteAverage = 8.5,
            voteCount = 4000
        )

        every {
            repository.getMovieDetails(movieId, source)
        } returns flow {
            delay(10)
            emit(Result.Success(movie))
        }

        viewModel.state.test {
            val initial = awaitItem()
            Truth.assertThat(initial.isLoading).isFalse()

            viewModel.onAction(
                MovieDetailAction.OnLoadMovie(movieId, source)
            )

            val loading = awaitItem()
            Truth.assertThat(loading.isLoading).isTrue()

            val success = awaitItem()
            Truth.assertThat(success.isLoading).isFalse()
            Truth.assertThat(success.movie?.title).isEqualTo("Dune: Part Two")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when load movie from search then emits loading then success`() = runTest {
        val movieId = 99
        val source = MovieSource.SEARCH

        val movie = Movie(
            id = movieId,
            title = "Interstellar",
            overview = "Space exploration",
            posterPath = null,
            backdropPath = null,
            releaseDate = "2014",
            voteAverage = 8.6,
            voteCount = 5000
        )

        every {
            repository.getMovieDetails(movieId, source)
        } returns flow {
            delay(10)
            emit(Result.Success(movie))
        }

        viewModel.state.test {
            awaitItem()

            viewModel.onAction(
                MovieDetailAction.OnLoadMovie(movieId, source)
            )

            awaitItem()

            val success = awaitItem()
            Truth.assertThat(success.movie?.title).isEqualTo("Interstellar")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when load movie from list fails then error event emitted`() = runTest {
        val movieId = 1
        val source = MovieSource.LIST

        every {
            repository.getMovieDetails(movieId, source)
        } returns flowOf(Result.Failure(DataError.Remote.UNKNOWN))

        viewModel.events.test {
            viewModel.onAction(
                MovieDetailAction.OnLoadMovie(movieId, source)
            )

            val event = awaitItem()
            Truth.assertThat(event)
                .isInstanceOf(MovieDetailEvent.OnError::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when load movie from search fails then error event emitted`() = runTest {
        val movieId = 2
        val source = MovieSource.SEARCH

        every {
            repository.getMovieDetails(movieId, source)
        } returns flowOf(Result.Failure(DataError.Remote.NO_INTERNET))

        viewModel.events.test {
            viewModel.onAction(
                MovieDetailAction.OnLoadMovie(movieId, source)
            )

            val event = awaitItem()
            Truth.assertThat(event)
                .isInstanceOf(MovieDetailEvent.OnError::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when back click then back event emitted`() = runTest {
        viewModel.events.test {
            viewModel.onAction(MovieDetailAction.OnBackClick)

            val event = awaitItem()
            Truth.assertThat(event).isEqualTo(MovieDetailEvent.OnBack)

            cancelAndIgnoreRemainingEvents()
        }
    }
}