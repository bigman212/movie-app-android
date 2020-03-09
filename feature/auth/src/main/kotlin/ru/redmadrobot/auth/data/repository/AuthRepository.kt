package ru.redmadrobot.auth.data.repository

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.redmadrobot.auth.domain.entities.UserCredentials
import ru.redmadrobot.core.network.MoviesService
import ru.redmadrobot.core.network.entities.AuthResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val moviesApi: MoviesService) {

    fun loginWith(credentials: UserCredentials): Observable<AuthResponse> {
        return moviesApi
            .login(credentials.toAuthRequest())
            .subscribeOn(Schedulers.io())
    }
}