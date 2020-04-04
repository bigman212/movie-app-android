package ru.redmadrobot.movie_list.domain

import io.reactivex.Single
import ru.redmadrobot.common.data.GenresRepository
import ru.redmadrobot.common.data.entity.Genre
import ru.redmadrobot.movie_list.Movie
import ru.redmadrobot.movie_list.search.MovieRepository
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

    private fun genreFromId(genreId: Int): Genre? = genresRepo.genreById(genreId)
}
