package com.example.movie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.domain.util.Result
import com.example.movie.domain.model.Movie
import com.example.movie.domain.movie.MovieService

class SearchPagingSource(
    private val service: MovieService,
    private val query: String
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return when (val result = service.searchMovies(query, page)) {
            is Result.Success -> {
                val movies = result.data
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (movies.isEmpty()) null else page + 1
                )
            }

            is Result.Failure -> {
                LoadResult.Error(Exception(result.error.name))
            }
        }
    }
}