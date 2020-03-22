package ru.redmadrobot.auth.domain.usecase

import android.annotation.SuppressLint
import io.reactivex.Single
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.core.network.SessionIdRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val sessionRepo: SessionIdRepository,
    private val authRepository: AuthRepository
) {

    fun login(login: String, password: String): Single<SessionIdResponse> = authRepository
        .loginWith(login, password)
        .map(this@AuthUseCase::saveSessionId)


    /**
     * Сохраняем полученный session_id в SharedPrefs согласно ТЗ
     */
    @SuppressLint("NewApi")
    private fun saveSessionId(sessionIdResponse: SessionIdResponse): SessionIdResponse {
        sessionRepo.saveSessionId(sessionIdResponse.sessionId)
        return sessionIdResponse
    }
}
