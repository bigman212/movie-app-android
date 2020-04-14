package ru.redmadrobot.profile.data

import io.reactivex.Completable
import io.reactivex.Single
import ru.redmadrobot.common.data.profile.AccountDetails
import ru.redmadrobot.common.data.profile.AccountRepository
import ru.redmadrobot.common.extensions.flatMapCompletableAction
import ru.redmadrobot.core.network.SessionIdRepository
import ru.redmadrobot.profile.data.entities.DeleteSessionRequest
import ru.redmadrobot.profile.data.entities.DeleteSessionResponse
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val sessionIdRepository: SessionIdRepository,
    private val accountRepository: AccountRepository,
    private val profileService: ProfileApi
) {
    fun logout(): Completable {
        val sessionId = sessionIdRepository.getSessionId()

        return if (sessionId != null) {
            profileService.deleteSession(DeleteSessionRequest(sessionId))
                .flatMapCompletableAction(this::deleteLocalSessionId)
                .onErrorComplete()
        } else Completable.complete()
    }

    fun getAccountDetails(): Single<AccountDetails> {
        return Single.fromCallable {
            accountRepository.currentAccount() ?: throw Throwable("Current account is null")
        }
    }

    private fun deleteLocalSessionId(response: DeleteSessionResponse) {
        if (response.sessionIsDeleted) sessionIdRepository.clear()
    }
}
