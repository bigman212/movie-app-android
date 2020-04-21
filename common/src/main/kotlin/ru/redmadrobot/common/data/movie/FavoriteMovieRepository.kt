package ru.redmadrobot.common.data.movie

import io.reactivex.Observable
import io.reactivex.Single
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.common.data.movie.entity.MarkMediaFavoriteRequest
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.core.network.DefaultResponse
import ru.redmadrobot.core.network.entities.WithPages
import ru.redmadrobot.persist.dao.FavoriteMovieDao
import ru.redmadrobot.persist.dao.FavoriteMovieWithGenreDao
import timber.log.Timber
import javax.inject.Inject

class FavoriteMovieRepository @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val movieWithGenreDao: FavoriteMovieWithGenreDao,
    private val api: MovieApi,

    private val movieRepository: MovieRepository,
    private val genresRepository: GenresRepository
) {
    fun markMovieAsFavorite(
        accountId: Long,
        sessionId: String,
        body: MarkMediaFavoriteRequest
    ): Single<DefaultResponse> {
        return api.markMovieAsFavorite(accountId, sessionId, body)
    }

    fun favoriteMovies(accountId: Long, sessionId: CharSequence): Single<WithPages<Movie>> {
        return api.favoriteMovies(accountId, sessionId)
            .flatMap(this::fillMoviesWithInformation)
    }

    private fun fillMoviesWithInformation(response: WithPages<Movie>): Single<WithPages<Movie>> {
        return Observable.fromIterable(response.results)
            .flatMapSingle(this::fillMoviesWithRuntimeAndGenres)
            .toList()
            .map { response.copy(results = it) }
    }

    private fun fillMoviesWithRuntimeAndGenres(movieWithoutInfo: Movie): Single<Movie> {
        return movieRepository.movieDetailsById(movieWithoutInfo.id)
            .map { movieDetail -> movieWithoutInfo.copy(runtime = movieDetail.runtime ?: 0) }
            .map(this::fillMoviesWithGenres)
            .onErrorReturnItem(movieWithoutInfo)
    }

    private fun fillMoviesWithGenres(movie: Movie): Movie {
        val movieGenres = movie.genreIds.mapNotNull(genresRepository::genreById)
        return movie.copy(genres = movieGenres)
    }

    fun isMovieInFavorites(movieId: Long, sessionId: CharSequence): Single<Boolean> {
        return api.movieAccountStates(movieId, sessionId)
            .map { it.isInFavorites }
            .doOnError { Timber.e(it) }
    }
}
