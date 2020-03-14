package ru.redmadrobot.core.network.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(val authorized: Boolean)