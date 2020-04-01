package ru.redmadrobot.movie_list

import io.reactivex.Single
import retrofit2.http.GET
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.core.network.entities.WithPages
import ru.redmadrobot.movie_list.adapters.Movie

interface MovieSearchService {

    @GET(NetworkRouter.MOVIES_POPULAR)
    fun popularMovies(): Single<WithPages<Movie>>
}
