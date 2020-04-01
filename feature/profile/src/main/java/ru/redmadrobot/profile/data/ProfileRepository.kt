package ru.redmadrobot.profile.data

import io.reactivex.Completable
import ru.redmadrobot.core.network.SessionIdRepository
import ru.redmadrobot.profile.data.entities.DeleteSessionRequest
import ru.redmadrobot.profile.data.entities.DeleteSessionResponse
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val sessionIdRepository: SessionIdRepository,
    private val profileService: ProfileService
) {
    fun logout(): Completable {
        val sessionId = sessionIdRepository.getSessionId()

        return if (sessionId != null) {
            profileService.deleteSession(DeleteSessionRequest(sessionId))
                .map(this::deleteLocalSessionId)
                .flatMapCompletable {
                    Completable.complete()
                }
        } else Completable.complete()
    }

    private fun deleteLocalSessionId(response: DeleteSessionResponse): DeleteSessionResponse {
        if (response.sessionIsDeleted) sessionIdRepository.clear()
        return response
    }
}
