package ru.redmadrobot.common.data.movie

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import ru.redmadrobot.common.data.genre.Genre
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.common.data.movie.entity.MarkMediaFavoriteRequest
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.core.network.DefaultResponse
import ru.redmadrobot.core.network.entities.WithPages
import ru.redmadrobot.persist.dao.FavoriteMovieDao
import ru.redmadrobot.persist.dao.FavoriteMovieWithGenreDao
import ru.redmadrobot.persist.entities.MovieToGenreCrossRef
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
        return fetchAndSaveMovies(accountId, sessionId)
            .andThen(movieWithGenreDao.loadAll())
            .flattenAsObservable { it }
            .map(Movie.Companion::fromFavoriteMovieDb)
            .toList()
            .map { movieList -> WithPages.createFromResults(1, movieList) }
    }

    private fun fetchAndSaveMovies(accountId: Long, sessionId: CharSequence): Completable {
        return api.favoriteMovies(accountId, sessionId)
            .flatMap(this::fillMoviesWithInformation)
            .map { it.results }
            .flattenAsObservable { it }
            .flatMapCompletable { movie -> saveFavoriteMovies(movie, movie.genreIds) }
    }

    private fun saveFavoriteMovies(fetchedFavoriteMovie: Movie, genreIds: List<Long>): Completable {
        val crossRefs = genreIds.map { genreId -> MovieToGenreCrossRef(fetchedFavoriteMovie.id, genreId) }
        val dbEntity = fetchedFavoriteMovie.toFavoriteMovieDb()

        return favoriteMovieDao
            .insert(dbEntity)
            .doOnComplete { Timber.d("${fetchedFavoriteMovie.id} saved!") }
            .andThen(movieWithGenreDao.insertAll(crossRefs))
            .doOnComplete { Timber.d("$crossRefs cross refs saved!") }
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
            .flatMap(this::fillMoviesWithGenres)
            .onErrorReturnItem(movieWithoutInfo)
    }

    private fun fillMoviesWithGenres(movie: Movie): Single<Movie> {
        return genresRepository.allGenresByIds(movie.genreIds)
            .flattenAsObservable { it }
            .map(Genre.Companion::fromGenreDb)
            .toList()
            .map { movie.copy(genres = it) }
    }

    fun isMovieInFavorites(movieId: Long): Single<Boolean> {
        val movieIsNotFavoriteResponse = SingleSource<Boolean> { Single.just(false) }
        return favoriteMovieDao.findById(movieId)
            .map { true }
            .switchIfEmpty(movieIsNotFavoriteResponse)
    }
}
