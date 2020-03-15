package ru.redmadrobot.core.network.entities

sealed class HttpException(override val message: String) : Throwable(message) {
    data class NoNetworkConnection(override val message: String = "No internet connection") : HttpException(message)

    data class BadRequest(
        val errorResponse: ErrorResponse, override val message: String = errorResponse.statusMessage
    ) : HttpException(message)
}