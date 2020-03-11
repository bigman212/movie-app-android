package ru.redmadrobot.auth.viewmodel

data class AuthViewState(
    val isFetching: Boolean = false,
    val isButtonEnabled: Boolean = false,

    // с сервера 200, пользователь авторизован
    val isAuthorized: Boolean = false,
    val error: Throwable? = null
) {
    fun fetchingState(): AuthViewState = copy(isFetching = true, error = null)
    fun buttonChangedState(isEnabled: Boolean): AuthViewState = copy(isButtonEnabled = isEnabled)
    fun authorizedState(): AuthViewState = copy(isAuthorized = true, isFetching = false)
    fun errorState(error: Throwable): AuthViewState = copy(error = error)
}