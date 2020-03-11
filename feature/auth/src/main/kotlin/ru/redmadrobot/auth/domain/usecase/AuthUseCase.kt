package ru.redmadrobot.auth.domain.usecase

import io.reactivex.Single
import ru.redmadrobot.auth.data.entities.UserCredentials
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.core.network.entities.AuthResponse
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) {
    fun login(login: String, password: String): Single<AuthResponse> {
        val credentials = UserCredentials(login, password)
        return authRepository
            .loginWith(credentials)
    }
}