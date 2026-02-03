package com.example.movie.presentation.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.core.presentation.ui.util.asUiText
import com.example.movie.domain.movie.MovieRepository
import com.example.movie.presentation.mapper.toDetailUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state = _state.asStateFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            MovieDetailState()
        )

    private val _events = Channel<MovieDetailEvent>()
    val events = _events.receiveAsFlow()

    private var currentMovieId: Int? = null

    fun onAction(action: MovieDetailAction) {
        when (action) {
            is MovieDetailAction.OnLoadMovie -> {
                currentMovieId = action.movieId
                loadMovie(action.movieId)
            }

            MovieDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    _events.send(MovieDetailEvent.OnBack)
                }
            }
        }
    }

    private fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            repository.getMovieDetails(movieId)
                .collect { result ->
                    result.onSuccess { movie ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                movie = movie.toDetailUi(),
                            )
                        }
                    }.onFailure { error ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                        _events.send(MovieDetailEvent.OnError(error.asUiText()))
                    }
                }
        }
    }
}