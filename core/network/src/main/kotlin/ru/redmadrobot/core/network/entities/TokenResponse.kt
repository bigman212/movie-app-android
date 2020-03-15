package ru.redmadrobot.core.network.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @field:Json(name = "success") val success: Boolean,
    @field:Json(name = "expires_at") val expiresAt: String,
    @field:Json(name = "request_token") val requestToken: String
)