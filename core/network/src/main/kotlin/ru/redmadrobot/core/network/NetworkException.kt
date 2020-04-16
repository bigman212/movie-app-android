package ru.redmadrobot.core.network

/**
 * Иерархия ошибок от сервера
 * некоторые из предков принимают в себя [DefaultResponse] - с нужной UI информацией об ошибке
 */
sealed class NetworkException(override val message: String) : Throwable(message) {
    data class NoNetworkConnection(override val message: String = "No internet connection") : NetworkException(message)


    class BadRequest(
        errorResponse: DefaultResponse,
        override val message: String = errorResponse.statusMessage
    ) : NetworkException(message)

    class Unauthorized(
        errorResponse: DefaultResponse,
        override val message: String = errorResponse.statusMessage
    ) : NetworkException(message)

    class Unknown(
        errorResponse: DefaultResponse,
        override val message: String = errorResponse.statusMessage
    ) : NetworkException(message)
}
