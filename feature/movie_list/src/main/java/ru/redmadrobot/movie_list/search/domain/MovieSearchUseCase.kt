package ru.redmadrobot.movie_list.search.domain

import io.reactivex.Single
import ru.redmadrobot.common.data.movie.MovieRepository
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.core.network.entities.WithPages
import javax.inject.Inject

class MovieSearchUseCase @Inject constructor(
    private val moviesRepo: MovieRepository
) {
    fun searchMovie(title: CharSequence): Single<List<Movie>> {
        return moviesRepo
            .findByTitle(title)
            .map(WithPages<Movie>::results)
    }
}
