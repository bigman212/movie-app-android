package ru.redmadrobot.auth.domain.usecase

import android.annotation.SuppressLint
import io.reactivex.Single
import ru.redmadrobot.auth.data.entities.UserCredentials
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.core.network.SessionIdRepository
import ru.redmadrobot.core.network.entities.SessionIdResponse
import ru.redmadrobot.core.network.entities.TvShow
import ru.redmadrobot.core.network.entities.WithPages
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val sessionRepo: SessionIdRepository,
    private val authRepository: AuthRepository
) {

    fun login(login: String, password: String): Single<SessionIdResponse> {
        val credentials = UserCredentials(login, password)
        return authRepository
            .loginWith(credentials)
            .map(this@AuthUseCase::saveSessionId)
    }

    // для проверки авторизации и вообще проходят ли запросы
    fun popularTvShows(): Single<WithPages<TvShow>> {
        return authRepository.popularTvShows()
    }

    /**
     * Сохраняем полученный session_id в SharedPrefs согласно ТЗ
     */
    @SuppressLint("NewApi")
    private fun saveSessionId(sessionIdResponse: SessionIdResponse): SessionIdResponse {
        sessionRepo.saveSessionId(sessionIdResponse.sessionId)
        return sessionIdResponse
    }
}