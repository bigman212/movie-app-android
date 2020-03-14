package ru.redmadrobot.core.network.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @field:Json(name = "status_message") val statusMessage: String = "Неизвестная ошибка с сервера",
    @field:Json(name = "status_code") val statusCode: Int = -1
) {
    companion object {
        fun default(): ErrorResponse = ErrorResponse(statusCode = 400)
    }
}