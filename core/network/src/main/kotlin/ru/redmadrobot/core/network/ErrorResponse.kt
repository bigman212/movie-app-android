package ru.redmadrobot.core.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Класс-обертка над ошибками от сервера.
 * При http code != 200, сервер формирует JSON { status_message: "...", status_code: <code> }
 * [ErrorInterceptor] при получении ошибки формирует эту обертку и передает дальше в UI слой
 */
@JsonClass(generateAdapter = true)
data class ErrorResponse(
    /**
     * Сообщение от сервера о причине провала запроса в ENG
     */
    @field:Json(name = "status_message")
    val statusMessage: String,
    /**
     * Код обозначающий тип ошибки, подробнее: [StatusCode]
     */
    @field:Json(name = "status_code")
    val statusCode: Int
)
