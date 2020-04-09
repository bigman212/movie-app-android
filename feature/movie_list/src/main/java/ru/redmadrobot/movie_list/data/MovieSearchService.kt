package ru.redmadrobot.movie_list.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.redmadrobot.common.adapters.Movie
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.core.network.entities.WithPages
import ru.redmadrobot.movie_list.data.entity.MovieDetail

interface MovieSearchService {
    @GET(NetworkRouter.SEARCH_MOVIE)
    fun moviesByQuery(@Query("query") movieTitle: CharSequence): Single<WithPages<Movie>>

    @GET(NetworkRouter.MOVIE_DETAILS)
    fun movieDetail(@Path("movie_id") movieId: Int): Single<MovieDetail>
}
