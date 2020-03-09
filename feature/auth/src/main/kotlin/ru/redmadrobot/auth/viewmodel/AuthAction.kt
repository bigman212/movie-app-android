package ru.redmadrobot.auth.viewmodel

sealed class AuthAction {
    object Fetching : AuthAction()
    object WrongCredentialsError : AuthAction()
    class UnknownError(val unknownError: Throwable) : AuthAction()
    object Authorize : AuthAction()
}