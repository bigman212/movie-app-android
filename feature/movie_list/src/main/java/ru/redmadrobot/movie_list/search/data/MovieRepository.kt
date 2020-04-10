package ru.redmadrobot.movie_list.search.data

import io.reactivex.Single
import ru.redmadrobot.common.data.movie.MovieApi
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.core.network.entities.WithPages
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: MovieApi
) {
    fun findByTitle(name: CharSequence): Single<List<Movie>> {
        return api
            .moviesByQuery(name)
            .map(WithPages<Movie>::results)
    }

    fun movieDetailsById(movieId: Int): Single<MovieDetail> {
        return api.movieDetail(movieId)
    }
}
