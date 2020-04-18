package ru.redmadrobot.auth.login.viewmodel

data class LoginViewState(
    val isFetching: Boolean = false,
    val isButtonEnabled: Boolean = false,

    // с сервера 200, пользователь авторизован
    val isAuthorized: Boolean = false,
    val errorMessage: String? = null
) {
    fun fetchingState(): LoginViewState = copy(isFetching = true, errorMessage = null)
    fun buttonChangedState(isEnabled: Boolean): LoginViewState = copy(isButtonEnabled = isEnabled)
    fun authorizedState(): LoginViewState = copy(isAuthorized = true, isFetching = false)
    fun errorState(error: String?): LoginViewState = copy(errorMessage = error, isFetching = false)
}
