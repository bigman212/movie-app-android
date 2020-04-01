package ru.redmadrobot.profile

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.profile.data.ProfileRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val profileRepo: ProfileRepository
) : BaseViewModel() {

    val liveState: MutableLiveData<ProfileViewState> = MutableLiveData(createInitialState())
    private var state: ProfileViewState by liveState.delegate()

    fun onLogoutButtonClicked() {
        profileRepo.logout()
            .scheduleIoToUi(schedulersProvider)
            .subscribe(
                {
                    state = state.logoutState()
                },
                {
                    state = state.errorState(it.message)
                }
            ).disposeOnCleared()
    }

    private fun createInitialState() = ProfileViewState()
}

