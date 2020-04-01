package ru.redmadrobot.movie_list.search

import io.reactivex.Single
import ru.redmadrobot.core.network.entities.WithPages
import ru.redmadrobot.movie_list.Movie
import ru.redmadrobot.movie_list.data.MovieSearchService
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: MovieSearchService
) {
    fun findByTitle(name: CharSequence): Single<List<Movie>> {
        return api
            .moviesByQuery(name)
            .map(WithPages<Movie>::results)
    }
}
