package ru.redmadrobot.common.data.movie

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.redmadrobot.common.data.movie.entity.MarkMediaFavoriteRequest
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.core.network.DefaultResponse
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.core.network.entities.WithPages

interface MovieApi {
    @GET(NetworkRouter.SEARCH_MOVIE)
    fun moviesByTitle(@Query("query") movieTitle: CharSequence): Single<WithPages<Movie>>

    @GET(NetworkRouter.MOVIE_DETAILS)
    fun movieDetail(@Path("movie_id") movieId: Long): Single<MovieDetail>

    @GET(NetworkRouter.FAVORITE_MOVIES)
    fun favoriteMovies(
        @Path("account_id") accountId: Long,
        @Query("session_id") sessionId: CharSequence
    ): Single<WithPages<Movie>>

    @POST(NetworkRouter.FAVORITE)
    fun markMovieAsFavorite(
        @Path("account_id") accountId: Long,
        @Query("session_id") sessionId: CharSequence,
        @Body body: MarkMediaFavoriteRequest
    ): Single<DefaultResponse>

    @GET(NetworkRouter.MOVIE_ACCOUNT_STATE)
    fun movieAccountStates(
        @Path("movie_id") movieId: Long,
        @Query("session_id") sessionId: CharSequence
    ): Single<MovieAccountState>
}
