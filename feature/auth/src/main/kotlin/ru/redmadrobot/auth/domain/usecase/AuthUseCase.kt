package ru.redmadrobot.auth.domain.usecase

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.auth.domain.entities.UserCredentials
import ru.redmadrobot.core.network.entities.AuthResponse
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) {
    fun login(login: String, password: String): Observable<AuthResponse> {
        return authRepository
            .loginWith(UserCredentials(login, password))
            .observeOn(AndroidSchedulers.mainThread())
    }
}