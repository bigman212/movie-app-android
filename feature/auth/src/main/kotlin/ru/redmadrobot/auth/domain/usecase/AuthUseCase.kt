package ru.redmadrobot.auth.domain.usecase

import android.annotation.SuppressLint
import io.reactivex.Single
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.core.network.SessionIdRepository
import timber.log.Timber
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepo: AuthRepository,
    private val sessionRepo: SessionIdRepository,
    private val genresRepo: GenresRepository
) {

    fun login(login: String, password: String): Single<SessionIdResponse> {
        return authRepo
            .loginWith(login, password)
            .map(this@AuthUseCase::saveSessionId)
            .flatMap(this::fetchGenres)
    }


    /**
     * Сохраняем полученный session_id в SharedPrefs согласно ТЗ
     */
    @SuppressLint("NewApi")
    private fun saveSessionId(sessionIdResponse: SessionIdResponse): SessionIdResponse {
        sessionRepo.saveSessionId(sessionIdResponse.sessionId)
        return sessionIdResponse
    }

    private fun fetchGenres(sessionIdResponse: SessionIdResponse): Single<SessionIdResponse> {
        return genresRepo.fetchGenresAndSave()
            .doOnError { Timber.e(it) }
            .onErrorComplete()
            .andThen(Single.just(sessionIdResponse))
    }
}
