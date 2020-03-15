package ru.redmadrobot.core.network

import com.squareup.moshi.Moshi
import ru.redmadrobot.core.network.entities.AuthResponse
import ru.redmadrobot.core.network.entities.ErrorResponse
import javax.inject.Inject


class NetworkRouter @Inject constructor(private val moshi: Moshi) {
    companion object Routes {
        const val BASE_URL = "https://api.themoviedb.org"

        private const val api_number = "/3"

        private const val auth = "$api_number/authentication"
        const val AUTH_TOKEN_NEW = "${auth}/token/new"
        const val AUTH_VALIDATE_TOKEN = "${auth}/token/validate_with_login"
        const val AUTH_CREATE_SESSION_ID = "${auth}/session/new"

        private const val tv = "$api_number/tv"
        const val TV_POPULAR = "$tv/popular"
    }

    fun mockedSuccessResponseByUrl(url: String): String = when {
        url.endsWith(AUTH_TOKEN_NEW) -> {
            toJson(AuthResponse(authorized = true))
        }

        else -> toJson(ErrorResponse.default())
    }

    fun mockedErrorResponseByUrl(url: String): String = when {
        url.endsWith(AUTH_TOKEN_NEW) -> {
            val serverError = ErrorResponse(
                "Invalid password or login",
                ErrorResponse.StatusCode.INVALID_CREDENTIALS
            )
            toJson(serverError)
        }
        else -> toJson(ErrorResponse.default())
    }

    private inline fun <reified T> toJson(entity: T) = moshi.adapter(T::class.java).toJson(entity)
}