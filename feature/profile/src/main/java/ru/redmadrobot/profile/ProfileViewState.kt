package ru.redmadrobot.profile

data class ProfileViewState(
    val isFetching: Boolean = false,
    val isLogoutButtonEnabled: Boolean = true
) {
    fun fetchingState() = copy(isFetching = true)
    fun buttonChangedState(isEnabled: Boolean) = copy(isLogoutButtonEnabled = isEnabled)
}
