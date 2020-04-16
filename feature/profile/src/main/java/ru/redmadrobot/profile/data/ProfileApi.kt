package ru.redmadrobot.profile.data

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.HTTP
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.profile.data.entities.DeleteSessionRequest
import ru.redmadrobot.profile.data.entities.DeleteSessionResponse

interface ProfileApi {
    /**
     * If you would like to delete (or "logout") from a session, call this method with a valid session ID.
     */
    @HTTP(method = "DELETE", path = NetworkRouter.AUTH_DELETE_SESSION, hasBody = true)
    fun deleteSession(@Body body: DeleteSessionRequest): Single<DeleteSessionResponse>
}

