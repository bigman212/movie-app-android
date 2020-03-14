package ru.redmadrobot.core.network.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthRequest(
    @field:Json(name = "login") val login: String,
    @field:Json(name = "password") val password: String
)