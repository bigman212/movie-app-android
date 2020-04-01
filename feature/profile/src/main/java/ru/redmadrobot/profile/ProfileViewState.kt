package ru.redmadrobot.profile

data class ProfileViewState(
    val isFetching: Boolean = false,
    val isButtonEnabled: Boolean = true,

    val isLogout: Boolean = false,
    val errorMessage: String? = null
) {
    fun fetchingState() = copy(isFetching = true, errorMessage = null)
    fun buttonChangedState(isEnabled: Boolean) = copy(isButtonEnabled = isEnabled)
    fun logoutState() = copy(isLogout = true, isFetching = false)
    fun errorState(error: String?) = copy(errorMessage = error, isFetching = false, isButtonEnabled = true)
}
