package ru.redmadrobot.common.data.movie

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.core.network.entities.WithPages

interface MovieApi {
    @GET(NetworkRouter.SEARCH_MOVIE)
    fun moviesByQuery(@Query("query") movieTitle: CharSequence): Single<WithPages<Movie>>

    @GET(NetworkRouter.MOVIE_DETAILS)
    fun movieDetail(@Path("movie_id") movieId: Int): Single<MovieDetail>

    @GET(NetworkRouter.FAVORITE_MOVIES)
    fun favoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: CharSequence
    ): Single<WithPages<Movie>>

}
