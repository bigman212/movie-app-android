package ru.redmadrobot.profile

data class ProfileViewState(
    val isFetching: Boolean = false,
    val isLogoutButtonEnabled: Boolean = true
) {
    fun fetchingState() = copy(isFetching = true, isLogoutButtonEnabled = false)
    fun fetchingFinishedState() = copy(isFetching = false, isLogoutButtonEnabled = true)
}
