package ru.redmadrobot.auth.viewmodel

data class AuthViewState(
    val isLoading: Boolean = false,

    val isAuthorized: Boolean = false,

    val wrongCredentials: Boolean = false,
    val unknownError: Throwable? = null
) {
    // можем ли мы убрать этот бойлерплейт?
    fun fetchingState(): AuthViewState = this.copy(
        isLoading = true,
        isAuthorized = false,
        wrongCredentials = false,
        unknownError = null
    )

    fun authorizedState(): AuthViewState = this.copy(
        isLoading = false,
        isAuthorized = true,
        wrongCredentials = false,
        unknownError = null
    )

    fun wrongCredentialsErrorState(): AuthViewState = this.copy(
        isLoading = false,
        isAuthorized = false,
        wrongCredentials = true,
        unknownError = null
    )

    fun unknownErrorState(unknownError: Throwable): AuthViewState = this.copy(
        isLoading = false,
        isAuthorized = false,
        wrongCredentials = false,
        unknownError = unknownError
    )
}