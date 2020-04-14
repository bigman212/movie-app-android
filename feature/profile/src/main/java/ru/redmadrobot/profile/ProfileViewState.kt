package ru.redmadrobot.profile

import ru.redmadrobot.common.data.profile.AccountDetails

data class ProfileViewState(
    val isFetching: Boolean = false,
    val isLogoutButtonEnabled: Boolean = true,

    val account: AccountDetails? = null
) {
    fun fetchingState() = copy(isFetching = true, isLogoutButtonEnabled = false)
    fun fetchingFinishedState() = copy(isFetching = false, isLogoutButtonEnabled = true)

    fun withAccountDetails(account: AccountDetails?) = copy(account = account)
}
