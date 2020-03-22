package ru.redmadrobot.auth.data.repository

import io.reactivex.Single
import ru.redmadrobot.auth.data.AuthService
import ru.redmadrobot.auth.data.entities.request.SessionIdRequest
import ru.redmadrobot.auth.data.entities.request.ValidateTokenRequest
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApi: AuthService) {

    fun loginWith(login: String, password: String): Single<SessionIdResponse> = authApi
        .newRequestToken()
        .map { ValidateTokenRequest(login, password, it.requestToken) }
        .flatMap(authApi::validateUser)
        .map { SessionIdRequest(it.requestToken) }
        .flatMap(authApi::createSessionId)
}
