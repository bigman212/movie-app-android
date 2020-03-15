package ru.redmadrobot.core.network.entities

import com.squareup.moshi.*
import timber.log.Timber

data class ErrorResponse(
    @field:Json(name = "status_message") val statusMessage: String,
    @field:Json(name = "status_code") val statusCode: StatusCode
) {

    enum class StatusCode(val code: Int) {
        INVALID_API_KEY(7), // Неправильный апи ключ
        INVALID_CREDENTIALS(30), // Неправильный логин или пароль

        INVALID_REQUEST_TOKEN(33), // истек срок годности request_token

        SESSION_DENIED(17), // истек срок годности session_id

        UNKNOWN_ERROR(-1);

        companion object {
            private val codeToEnumValueMap = values().associateBy(StatusCode::code)
            fun fromInt(code: Int) = codeToEnumValueMap[code]
        }
    }

    companion object {
        fun default(): ErrorResponse =
            ErrorResponse("Unknown error from server", StatusCode.UNKNOWN_ERROR)
    }
}

class ErrorResponseAdapter : JsonAdapter<ErrorResponse>() {
    companion object {
        val NAMES: JsonReader.Options = JsonReader.Options.of("status_code", "status_message")
    }

    @FromJson
    override fun fromJson(reader: JsonReader): ErrorResponse {
        reader.beginObject()

        // такого статуса в Enum может и не быть
        var statusCode: ErrorResponse.StatusCode? = null
        var statusMessage = ""
        while (reader.hasNext()) {
            when (reader.selectName(NAMES)) {
                0 -> statusCode = ErrorResponse.StatusCode.fromInt(reader.nextInt())
                1 -> statusMessage = reader.nextString()
                else -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }
        reader.endObject()

        if (statusCode == null || statusMessage.isBlank()) {
            Timber.e("[$statusCode, $statusMessage]")
            return ErrorResponse.default()
        }

        return ErrorResponse(statusMessage, statusCode)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: ErrorResponse?) {
        if (value == null) {
            throw JsonDataException("Required value is null!")
        }

        writer.beginObject()
        writer.name("status_code").value(value.statusCode.code)
        writer.name("status_message").value(value.statusMessage)
        writer.endObject()
    }
}