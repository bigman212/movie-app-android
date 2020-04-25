package ru.redmadrobot.auth.login.domain.usecase

import android.annotation.SuppressLint
import io.reactivex.Single
import ru.redmadrobot.auth.data.entities.response.AccountDetailsResponse
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.common.data.profile.AccountDetails
import ru.redmadrobot.common.data.profile.AccountRepository
import ru.redmadrobot.core.network.SessionIdRepository
import timber.log.Timber
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepo: AuthRepository,
    private val sessionRepo: SessionIdRepository,
    private val genresRepo: GenresRepository,
    private val accountRepo: AccountRepository
) {

    fun login(login: String, password: String): Single<SessionIdResponse> {
        return authRepo
            .loginWith(login, password)
            .map(this::saveSessionId)
            .flatMap(this::fetchGenres)
            .flatMap(this::fetchAccount)
    }

    private fun fetchAccount(sessionIdResponse: SessionIdResponse): Single<SessionIdResponse> {
        return authRepo.fetchAccountDetails(sessionIdResponse.sessionId)
            .doOnSuccess { this.saveAccountDetails(it) }
            .doOnError { Timber.e(it) }
            .map { sessionIdResponse }
    }

    private fun saveAccountDetails(account: AccountDetailsResponse) {
        val accountDetails = accountDetailsFromResponse(account)
        accountRepo.save(accountDetails)
        Timber.d("$account saved")
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

    private fun accountDetailsFromResponse(response: AccountDetailsResponse): AccountDetails {
        return AccountDetails(
            id = response.id,
            name = response.name,
            isAdult = response.shouldIncludeAdult,
            username = response.username,
            avatar = response.avatar.gravatar.hash
        )
    }
}
