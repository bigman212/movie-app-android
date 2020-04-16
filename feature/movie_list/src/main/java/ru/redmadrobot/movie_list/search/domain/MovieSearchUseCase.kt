package ru.redmadrobot.movie_list.search.domain

import io.reactivex.Observable
import io.reactivex.Single
import ru.redmadrobot.common.data.genre.Genre
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.common.data.movie.MovieRepository
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.core.network.entities.WithPages
import javax.inject.Inject

class MovieSearchUseCase @Inject constructor(
    private val moviesRepo: MovieRepository,
    private val genresRepo: GenresRepository
) {
    fun searchMovie(title: CharSequence): Single<List<Movie>> {
        return moviesRepo
            .findByTitle(title)
            .map(WithPages<Movie>::results)
            .flattenAsObservable { it }
            .map { movie ->
                val movieGenres = movie.genreIds.mapNotNull(this::genreStringFromId)
                movie.copy(genres = movieGenres)
            }
            .toList()
    }

    fun fetchMovieDetails(movieId: Int): Observable<MovieDetail> {
        return moviesRepo.movieDetailsById(movieId)
            .toObservable()
    }

    private fun genreStringFromId(genreId: Int): Genre? = genresRepo.genreById(genreId)
}
