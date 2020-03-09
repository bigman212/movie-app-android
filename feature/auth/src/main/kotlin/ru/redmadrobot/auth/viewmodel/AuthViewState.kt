package ru.redmadrobot.auth.viewmodel

data class AuthViewState(
    val isLoading: Boolean = false,

    val isAuthorized: Boolean = false,

    val wrongCredentials: Boolean = false,
    val unknownError: Throwable? = null
) {
    fun fetchingState(): AuthViewState = AuthViewState(isLoading = true)
    fun authorizedState(): AuthViewState = AuthViewState(isAuthorized = true)
    fun wrongCredentialsErrorState(): AuthViewState = AuthViewState(wrongCredentials = true)
    fun unknownErrorState(unknownError: Throwable): AuthViewState = AuthViewState(unknownError = unknownError)
}