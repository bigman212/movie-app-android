package ru.redmadrobot.core.network

import com.squareup.moshi.Moshi
import ru.redmadrobot.core.network.entities.AuthResponse
import ru.redmadrobot.core.network.entities.ErrorResponse
import javax.inject.Inject


class NetworkRouter @Inject constructor(private val moshi: Moshi) {
    companion object Routes {
        const val BASE_URL = "https://api.themoviedb.org"

        private const val auth = "/authentication"
        const val AUTH_TOKEN_NEW = "${auth}/token/new"
    }


    fun mockedResponseByUrl(url: String): String = when {
        url.endsWith(AUTH_TOKEN_NEW) -> {
            toJson(AuthResponse(authorized = true))
        }

        else -> toJson(ErrorResponse.default())
    }

    fun mockedErrorResponseByUrl(url: String): String = when {
        url.endsWith(AUTH_TOKEN_NEW) -> {
            val serverError = ErrorResponse("Неправильный логин или пароль", 400)
            toJson(serverError)
        }
        else -> toJson(ErrorResponse.default())
    }

    private inline fun <reified T> toJson(entity: T) = moshi.adapter(T::class.java).toJson(entity)

}