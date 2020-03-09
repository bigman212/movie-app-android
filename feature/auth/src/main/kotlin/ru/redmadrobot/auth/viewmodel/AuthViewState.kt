package ru.redmadrobot.auth.viewmodel

data class AuthViewState(
    val isLoading: Boolean = false,

    val isAuthorized: Boolean = false,

    val wrongCredentials: Boolean = false,
    val unknownError: Throwable? = null
) {
    fun fetchingState(): AuthViewState = this.copy(isLoading = true)
    fun authorizedState(): AuthViewState = this.copy(isAuthorized = true)
    fun wrongCredentialsErrorState(): AuthViewState = this.copy(wrongCredentials = true)

    fun unknownErrorState(unknownError: Throwable): AuthViewState = this.copy(unknownError = unknownError)
}