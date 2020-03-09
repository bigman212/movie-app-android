package ru.redmadrobot.auth.data.repository

import io.reactivex.Observable
import ru.redmadrobot.core.network.MoviesService
import ru.redmadrobot.core.network.entities.AuthResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val moviesApi: MoviesService) {

    fun login(login: String, password: String): Observable<AuthResponse> {
        return moviesApi.login()
    }
}