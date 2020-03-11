package ru.redmadrobot.auth.data.repository

import io.reactivex.Single
import ru.redmadrobot.auth.data.entities.UserCredentials
import ru.redmadrobot.core.network.MoviesService
import ru.redmadrobot.core.network.entities.AuthResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val moviesApi: MoviesService) {

    fun loginWith(credentials: UserCredentials): Single<AuthResponse> {
        return moviesApi
            .login(credentials.toAuthRequest())
    }
}