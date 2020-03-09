package ru.redmadrobot.auth.domain.entities

import ru.redmadrobot.core.network.entities.AuthRequest

data class UserCredentials(
    val login: String,
    val password: String
) {
    fun toAuthRequest(): AuthRequest = AuthRequest(login, password)
}