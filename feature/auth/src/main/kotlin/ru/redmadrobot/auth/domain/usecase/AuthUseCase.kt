package ru.redmadrobot.auth.domain.usecase

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.auth.domain.entities.UserCredentials
import ru.redmadrobot.core.network.entities.AuthResponse
import java.util.regex.Pattern
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) {
    fun login(login: String, password: String): Observable<AuthResponse> {

        val credentials = UserCredentials(login, password)
        if (credentialsAreNotValid(credentials)) {
            return Observable.error(IllegalArgumentException("Данные аккаунта не прошли валидацию"))
        }
        return authRepository
            .loginWith(credentials)
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun credentialsAreNotValid(credentials: UserCredentials): Boolean {
        //RFC 5322
        val emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
        val pattern = Pattern.compile(emailRegex);
        return pattern.matcher(credentials.login).matches().not()
    }
}