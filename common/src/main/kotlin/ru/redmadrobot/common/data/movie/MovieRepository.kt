package ru.redmadrobot.common.data.movie

import io.reactivex.Single
import ru.redmadrobot.common.data.movie.entity.MarkMovieFavoriteRequest
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.core.network.ErrorResponse
import ru.redmadrobot.core.network.entities.WithPages
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: MovieApi
) {
    fun findByTitle(name: CharSequence): Single<WithPages<Movie>> = api.moviesByQuery(name)

    fun movieDetailsById(movieId: Int): Single<MovieDetail> = api.movieDetail(movieId)

    fun addMovieToFavorite(
        accountId: Int,
        sessionId: String,
        body: MarkMovieFavoriteRequest
    ): Single<ErrorResponse> = api.markMovieAsFavorite(accountId, sessionId, body)
}
