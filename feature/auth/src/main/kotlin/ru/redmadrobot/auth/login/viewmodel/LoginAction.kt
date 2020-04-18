package ru.redmadrobot.auth.login.viewmodel

sealed class LoginAction {
    object Fetching : LoginAction()
    class EnableButton(val shouldBeEnabled: Boolean) : LoginAction()
    class Error(val uxError: String?) : LoginAction()
    object Authorize : LoginAction()
}
