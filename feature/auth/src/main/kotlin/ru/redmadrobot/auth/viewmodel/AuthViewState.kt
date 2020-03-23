package ru.redmadrobot.auth.viewmodel

data class AuthViewState(
    val isFetching: Boolean = false,
    val isButtonEnabled: Boolean = false,

    // с сервера 200, пользователь авторизован
    val isAuthorized: Boolean = false,
    val errorMessage: String? = null
) {
    fun fetchingState(): AuthViewState = copy(isFetching = true, errorMessage = null)
    fun buttonChangedState(isEnabled: Boolean): AuthViewState = copy(isButtonEnabled = isEnabled)
    fun authorizedState(): AuthViewState = copy(isAuthorized = true, isFetching = false)
    fun errorState(error: String?): AuthViewState = copy(errorMessage = error, isFetching = false)
}
