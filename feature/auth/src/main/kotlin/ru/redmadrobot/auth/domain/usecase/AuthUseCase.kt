package ru.redmadrobot.auth.domain.usecase

import ru.redmadrobot.auth.data.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) {
    fun login() {
        authRepository.login("", "")
            .subscribe()
    }
}