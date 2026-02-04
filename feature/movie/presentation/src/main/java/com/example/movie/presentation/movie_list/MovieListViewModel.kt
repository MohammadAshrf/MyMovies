@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.example.movie.presentation.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.core.presentation.ui.util.asUiText
import com.example.movie.domain.model.MovieSource
import com.example.movie.domain.movie.MovieRepository
import com.example.movie.presentation.mapper.toUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListViewModel(
    repository: MovieRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MovieListState())
    val state = _state.asStateFlow()
    val moviePagingFlow = _state
        .map { it.searchQuery }
        .distinctUntilChanged()
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getMovies()
            } else {
                repository.searchMovies(query)
            }
        }
        .map { pagingData -> pagingData.map { it.toUi() } }
        .cachedIn(viewModelScope)

    private val _events = Channel<MovieListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: MovieListAction) {
        when (action) {
            is MovieListAction.OnMovieClick -> {
                val source = if (state.value.isSearchActive) {
                    MovieSource.SEARCH
                } else {
                    MovieSource.LIST
                }

                viewModelScope.launch {
                    _events.send(MovieListEvent.OnMovieClick(action.movie.id, source))
                }
            }

            MovieListAction.OnToggleSearchClick -> {
                _state.update { state ->
                    if (state.isSearchActive) {
                        state.copy(isSearchActive = false, searchQuery = "")
                    } else {
                        state.copy(isSearchActive = true)
                    }
                }
            }

            is MovieListAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
        }
    }

    fun onPagingError(error: Throwable) {
        viewModelScope.launch {
            _events.send(
                MovieListEvent.OnError(error.asUiText())
            )
        }
    }
}