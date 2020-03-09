package ru.redmadrobot.core.network.entities

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)