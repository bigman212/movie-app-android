package ru.redmadrobot.profile.data

import io.reactivex.Completable
import ru.redmadrobot.common.extensions.flatMapCompletableAction
import ru.redmadrobot.core.network.SessionIdRepository
import ru.redmadrobot.profile.data.entities.DeleteSessionRequest
import ru.redmadrobot.profile.data.entities.DeleteSessionResponse
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val sessionIdRepository: SessionIdRepository,
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

    private fun deleteLocalSessionId(response: DeleteSessionResponse) {
        if (response.sessionIsDeleted) sessionIdRepository.clear()
    }
}
