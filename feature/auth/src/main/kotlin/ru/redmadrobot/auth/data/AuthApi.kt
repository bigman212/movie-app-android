package ru.redmadrobot.auth.data

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.redmadrobot.auth.data.entities.request.SessionIdRequest
import ru.redmadrobot.auth.data.entities.request.ValidateTokenRequest
import ru.redmadrobot.auth.data.entities.response.AccountDetailsResponse
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.entities.response.TokenResponse
import ru.redmadrobot.core.network.NetworkRouter

interface AuthApi {
    // порядок авторизации:
    // 1. /authentication/token/new
    // 2. /authentication/token/validate_with_login
    // 3. /authentication/session/new

    /**
     * Create a temporary request token that can be used to validate a TMDb user login.
     * @return request_token
     */
    @GET(NetworkRouter.AUTH_TOKEN_NEW)
    fun newRequestToken(): Single<TokenResponse>

    /**
     * This method allows an application to validate a request token by entering a username and password.
     * @param[body] user login, password and request_token received from [newRequestToken]
     */
    @POST(NetworkRouter.AUTH_VALIDATE_TOKEN)
    fun validateUser(@Body body: ValidateTokenRequest): Single<TokenResponse>

    /**
     * You can use this method to create a fully valid session ID once a user has validated the request token.
     * @param[body] - request_token received from [newRequestToken]
     * @return SessionId used to authenticate user actions
     */
    @POST(NetworkRouter.AUTH_CREATE_SESSION_ID)
    fun createSessionId(@Body body: SessionIdRequest): Single<SessionIdResponse>

    @GET(NetworkRouter.ACCOUNT_DETAILS)
    fun accountDetails(@Query("session_id") sessionId: CharSequence): Single<AccountDetailsResponse>
}
