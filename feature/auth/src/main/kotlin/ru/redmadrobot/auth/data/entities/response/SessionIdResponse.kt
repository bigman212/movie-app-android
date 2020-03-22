package ru.redmadrobot.auth.data.entities.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionIdResponse(
    @field:Json(name = "success")
    val success: Boolean,

    @field:Json(name = "session_id")
    val sessionId: String
)
