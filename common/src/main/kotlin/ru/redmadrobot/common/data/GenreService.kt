package ru.redmadrobot.common.data

import io.reactivex.Single
import retrofit2.http.GET
import ru.redmadrobot.common.data.entity.GenreResponse
import ru.redmadrobot.core.network.NetworkRouter

interface GenreService {
    @GET(NetworkRouter.GENRE_MOVIE_ALL)
    fun allMovieGenres(): Single<GenreResponse>
}
