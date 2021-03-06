package ru.redmadrobot.auth.data.entities.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionIdRequest(
    @field:Json(name = "request_token")
    val requestToken: String
)
