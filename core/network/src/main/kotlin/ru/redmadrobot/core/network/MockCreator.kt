package ru.redmadrobot.core.network

import com.squareup.moshi.Moshi
import javax.inject.Inject

/**
 * Создает моканые ответы на запросы в зависимости от переданного URL
 */
class MockCreator @Inject constructor(private val moshi: Moshi) {
    fun mockedSuccessResponseByUrl(url: String): String = when {
        url.endsWith(NetworkRouter.AUTH_TOKEN_NEW) -> {
            //todo: нужен ли доступ к entities в :auth чтобы замокать ответ, или строкового JSON будет достаточно?
            //language=JSON
            "{\"success\": true, \"expires_at\": 19999, \"request_token\": \"3fa1q1\"}"
        }

        else -> toJson(unknownServerResponse())
    }

    fun mockedErrorResponseByUrl(url: String): String = when {
        url.endsWith(NetworkRouter.AUTH_TOKEN_NEW) -> {
            val serverError = ErrorResponse(
                "Invalid password or login",
                NetworkErrorHandler.ErrorStatusCode.INVALID_CREDENTIALS
            )
            toJson(serverError)
        }
        else -> toJson(unknownServerResponse())
    }

    private fun unknownServerResponse() = ErrorResponse(
        "url not found",
        NetworkErrorHandler.ErrorStatusCode.UNKNOWN_ERROR
    )

    private inline fun <reified T> toJson(entity: T) = moshi.adapter(T::class.java).toJson(entity)
}
