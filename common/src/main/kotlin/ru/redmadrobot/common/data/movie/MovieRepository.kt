package ru.redmadrobot.common.data.movie

import io.reactivex.Observable
import io.reactivex.Single
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.core.network.entities.WithPages
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: MovieApi,
    private val genresRepository: GenresRepository
) {
    fun findByTitle(name: CharSequence): Single<WithPages<Movie>> {
        return api.moviesByTitle(name)
            .flatMap(this::fillMoviesWithInformation)
    }

    fun movieDetailsById(movieId: Long): Single<MovieDetail> {
        return api.movieDetail(movieId)
    }

    private fun fillMoviesWithInformation(response: WithPages<Movie>): Single<WithPages<Movie>> {
        return Observable.fromIterable(response.results)
            .flatMapSingle(this::fillMoviesWithRuntimeAndGenres)
            .toList()
            .map { response.copy(results = it) }
    }

    private fun fillMoviesWithRuntimeAndGenres(movieWithoutInfo: Movie): Single<Movie> {
        return movieDetailsById(movieWithoutInfo.id)
            .map { movieDetail -> movieWithoutInfo.copy(runtime = movieDetail.runtime ?: 0) }
            .map(this::fillMoviesWithGenres)
            .onErrorReturnItem(movieWithoutInfo)
    }

    private fun fillMoviesWithGenres(movie: Movie): Movie {
        val movieGenres = movie.genreIds.mapNotNull(genresRepository::genreById)
        return movie.copy(genres = movieGenres)
    }
}
