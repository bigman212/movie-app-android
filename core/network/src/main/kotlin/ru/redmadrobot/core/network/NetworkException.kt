package ru.redmadrobot.core.network

/**
 * Иерархия ошибок от сервера
 * некоторые из предков принимают в себя [ErrorResponse] - с нужной UI информацией об ошибке
 */
sealed class NetworkException(override val message: String) : Throwable(message) {
    data class NoNetworkConnection(override val message: String = "No internet connection") : NetworkException(message)


    class BadRequest(
        errorResponse: ErrorResponse,
        override val message: String = errorResponse.statusMessage
    ) : NetworkException(message)

    class Unauthorized(
        errorResponse: ErrorResponse,
        override val message: String = errorResponse.statusMessage
    ) : NetworkException(message)

    class Unknown(
        errorResponse: ErrorResponse,
        override val message: String = errorResponse.statusMessage
    ) : NetworkException(message)
}
