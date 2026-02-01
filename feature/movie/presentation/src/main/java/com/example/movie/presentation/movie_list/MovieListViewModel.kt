package com.example.movie.presentation.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.presentation.model.MovieUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MovieListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MovieListState()
        )

    private val _events = Channel<MovieListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: MovieListAction) {
        when (action) {
            is MovieListAction.OnMovieClick -> {
                viewModelScope.launch {
                    _events.send(MovieListEvent.OnMovieClick(action.movie.id))
                }
            }

            MovieListAction.OnSearchClick -> {
                viewModelScope.launch {
                    _events.send(MovieListEvent.OnSearchClick)
                }
            }

            MovieListAction.OnLoadMore -> {
                loadNextPage()
            }

            MovieListAction.OnRetryClick -> {
                loadInitialData()
            }

            MovieListAction.OnRetryPaginationClick -> {
                loadNextPage()
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            delay(1500)

            val dummyMovies = generateDummyMovies(1)

            _state.update {
                it.copy(
                    isLoading = false,
                    movies = dummyMovies,
                    page = 1,
                    endReached = false
                )
            }
        }
    }

    private fun loadNextPage() {
        if (_state.value.isPaginationLoading || _state.value.endReached) return

        viewModelScope.launch {
            _state.update { it.copy(isPaginationLoading = true, paginationError = null) }

            delay(1500)

            val nextPage = _state.value.page + 1
            val newMovies = generateDummyMovies(nextPage)

            val endReached = nextPage >= 5

            _state.update {
                it.copy(
                    isPaginationLoading = false,
                    movies = it.movies + newMovies,
                    page = nextPage,
                    endReached = endReached
                )
            }
        }
    }

    private fun generateDummyMovies(page: Int): List<MovieUi> {
        val startId = (page - 1) * 20
        return List(20) { index ->
            val id = startId + index
            MovieUi(
                id = id,
                title = "Movie Title $id (Page $page)",
                posterUrl = null,
                releaseYear = "202${id % 5}",
                rating = "9.5",
            )
        }
    }
}