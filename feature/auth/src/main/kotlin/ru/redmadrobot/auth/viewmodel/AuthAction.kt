package ru.redmadrobot.auth.viewmodel

sealed class AuthAction {
    object Fetching : AuthAction()
    class EnableButton(val shouldBeEnabled: Boolean) : AuthAction()
    class Error(val error: Throwable) : AuthAction()
    object Authorize : AuthAction()
}