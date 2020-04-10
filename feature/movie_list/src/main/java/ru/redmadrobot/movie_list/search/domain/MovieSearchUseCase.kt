package ru.redmadrobot.movie_list.search.domain

import io.reactivex.Observable
import io.reactivex.Single
import ru.redmadrobot.common.data.genre.Genre
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.movie_list.search.data.MovieRepository
import javax.inject.Inject

class MovieSearchUseCase @Inject constructor(
    private val moviesRepo: MovieRepository,
    private val genresRepo: GenresRepository
) {
    fun searchMovie(title: CharSequence): Single<List<Movie>> {
        return moviesRepo
            .findByTitle(title)
            .flattenAsObservable { it }
            .map { movie ->
                val movieGenres = movie.genreIds.mapNotNull(this::genreFromId)
                movie.copy(genres = movieGenres)
            }
            .toList()
    }

    fun fetchMovieDetails(movieId: Int): Observable<MovieDetail> {
        return moviesRepo.movieDetailsById(movieId)
            .toObservable()
    }

    private fun genreFromId(genreId: Int): Genre? = genresRepo.genreById(genreId)
}
