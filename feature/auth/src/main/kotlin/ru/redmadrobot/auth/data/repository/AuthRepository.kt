package ru.redmadrobot.auth.data.repository

import io.reactivex.Single
import ru.redmadrobot.auth.data.entities.UserCredentials
import ru.redmadrobot.core.network.MoviesService
import ru.redmadrobot.core.network.entities.*
import javax.inject.Inject

class AuthRepository @Inject constructor(private val moviesApi: MoviesService) {

    fun loginWith(credentials: UserCredentials): Single<SessionIdResponse> {
        return moviesApi
            .newRequestToken()
            .map { ValidateTokenRequest(credentials.login, credentials.password, it.requestToken) }
            .flatMap(moviesApi::validateUser)
            .map { SessionIdRequest(it.requestToken) }
            .flatMap(moviesApi::createSessionId)
    }

    fun popularTvShows(): Single<WithPages<TvShow>> {
        return moviesApi.popularTvShows()
    }
}