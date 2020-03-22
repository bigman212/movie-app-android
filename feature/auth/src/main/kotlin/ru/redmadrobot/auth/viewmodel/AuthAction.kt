package ru.redmadrobot.auth.viewmodel

sealed class AuthAction {
    object Fetching : AuthAction()
    class EnableButton(val shouldBeEnabled: Boolean) : AuthAction()
    class Error(val uxError: String?) : AuthAction()
    object Authorize : AuthAction()
}
