package ru.redmadrobot.common.data.genre

import io.reactivex.Single
import retrofit2.http.GET
import ru.redmadrobot.core.network.NetworkRouter

interface GenreApi {
    @GET(NetworkRouter.GENRE_MOVIE_ALL)
    fun allMovieGenres(): Single<GenreResponse>
}
