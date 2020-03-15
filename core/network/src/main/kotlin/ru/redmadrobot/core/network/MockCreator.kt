package ru.redmadrobot.core.network

import com.squareup.moshi.Moshi
import ru.redmadrobot.core.network.entities.AuthResponse
import javax.inject.Inject

/**
 * Создает моканые ответы на запросы в зависимости от переданного URL
 */
class MockCreator @Inject constructor(private val moshi: Moshi) {
    fun mockedSuccessResponseByUrl(url: String): String = when {
        url.endsWith(NetworkRouter.AUTH_TOKEN_NEW) -> {
            toJson(AuthResponse(authorized = true))
        }

        else -> toJson(ErrorResponse.unknown())
    }

    fun mockedErrorResponseByUrl(url: String): String = when {
        url.endsWith(NetworkRouter.AUTH_TOKEN_NEW) -> {
            val serverError = ErrorResponse(
                "Invalid password or login",
                ErrorResponse.StatusCode.INVALID_CREDENTIALS
            )
            toJson(serverError)
        }
        else -> toJson(ErrorResponse.unknown())
    }

    private inline fun <reified T> toJson(entity: T) = moshi.adapter(T::class.java).toJson(entity)
}