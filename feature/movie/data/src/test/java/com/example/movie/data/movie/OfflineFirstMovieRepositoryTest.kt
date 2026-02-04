package com.example.movie.data.movie

import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.movie.data.mapper.toDomain
import com.example.movie.database.MyMoviesDatabase
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.entities.MovieEntity
import com.example.movie.domain.model.Movie
import com.example.movie.domain.model.MovieSource
import com.example.movie.domain.movie.MovieService
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OfflineFirstMovieRepositoryTest {

    private val db: MyMoviesDatabase = mockk(relaxed = true)
    private val dao: MovieDao = mockk(relaxed = true)
    private val service: MovieService = mockk(relaxed = true)

    private lateinit var repository: OfflineFirstMovieRepository

    @Before
    fun setup() {
        every { db.movieDao } returns dao
        repository = OfflineFirstMovieRepository(db, service)
    }

    @Test
    fun `when movie exists locally then cached movie is emitted first`() = runTest {
        val movieId = 1

        val entity = MovieEntity(
            id = movieId,
            title = "Cached",
            overview = "Cached",
            posterPath = null,
            backdropPath = null,
            releaseDate = null,
            voteAverage = 0.0,
            voteCount = null,
            orderIndex = 0
        )

        coEvery { dao.getMovieById(movieId) } returns entity
        coEvery { service.getMovieDetails(movieId) } returns Result.Failure(DataError.Remote.UNKNOWN)

        val emissions = mutableListOf<Result<Movie, DataError.Remote>>()

        repository.getMovieDetails(movieId, MovieSource.LIST)
            .take(1)
            .collect { emissions.add(it) }

        assertThat(emissions.first()).isEqualTo(Result.Success(entity.toDomain()))
    }

    @Test
    fun `when remote succeeds then movie is updated in database`() = runTest {
        val movieId = 1

        val localEntity = MovieEntity(
            id = movieId,
            title = "Old",
            overview = "Old",
            posterPath = null,
            backdropPath = null,
            releaseDate = null,
            voteAverage = 0.0,
            voteCount = null,
            orderIndex = 0
        )

        val remoteMovie = Movie(
            id = movieId,
            title = "New",
            overview = "New",
            posterPath = null,
            backdropPath = null,
            releaseDate = null,
            voteAverage = 0.0,
            voteCount = null
        )

        coEvery { dao.getMovieById(movieId) } returns localEntity
        coEvery { service.getMovieDetails(movieId) } returns Result.Success(remoteMovie)
        coEvery { dao.upsertMovies(any()) } returns Unit

        val emissions = mutableListOf<Result<Movie, DataError.Remote>>()

        repository.getMovieDetails(movieId, MovieSource.LIST)
            .take(2)
            .collect { emissions.add(it) }

        coVerify(exactly = 1) { dao.upsertMovies(any()) }
        assertThat(emissions.last()).isEqualTo(Result.Success(remoteMovie))
    }

    @Test
    fun `when movie is not cached then it should not be saved in database`() = runTest {
        val movieId = 42

        val remoteMovie = Movie(
            id = movieId,
            title = "Search Result",
            overview = "From search",
            posterPath = null,
            backdropPath = null,
            releaseDate = null,
            voteAverage = 0.0,
            voteCount = null
        )

        coEvery { dao.getMovieById(movieId) } returns null
        coEvery { service.getMovieDetails(movieId) } returns Result.Success(remoteMovie)

        val emissions = mutableListOf<Result<Movie, DataError.Remote>>()

        repository.getMovieDetails(movieId, MovieSource.SEARCH)
            .take(1)
            .collect { emissions.add(it) }

        coVerify(exactly = 0) { dao.upsertMovies(any()) }
        assertThat(emissions.first()).isEqualTo(Result.Success(remoteMovie))
    }

    @Test
    fun `when remote fails and movie not cached then failure is emitted`() = runTest {
        val movieId = 99

        coEvery { dao.getMovieById(movieId) } returns null
        coEvery { service.getMovieDetails(movieId) } returns Result.Failure(DataError.Remote.UNKNOWN)

        val emissions = mutableListOf<Result<Movie, DataError.Remote>>()

        repository.getMovieDetails(movieId, MovieSource.SEARCH)
            .take(1)
            .collect { emissions.add(it) }

        assertThat(emissions.first()).isInstanceOf(Result.Failure::class.java)
    }

    @Test
    fun `searchMovies returns paging flow`() = runTest {
        val flow = repository.searchMovies("shallows")
        assertThat(flow).isNotNull()
    }
}