package ru.redmadrobot.profile.data.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteSessionRequest(
    @field:Json(name = "session_id")
    val sessionId: String
)
