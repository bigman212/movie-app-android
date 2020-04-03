package ru.redmadrobot.profile

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.profile.data.ProfileUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val profileRepo: ProfileUseCase
) : BaseViewModel() {

    object LogoutEvent : Event

    val viewState = MutableLiveData(ProfileViewState())
    private var state: ProfileViewState by viewState.delegate()

    fun onLogoutButtonClicked() {
        profileRepo.logout()
            .doOnSubscribe {
                state = state.fetchingState().buttonChangedState(false)
            }
            .scheduleIoToUi(schedulersProvider)
            .doOnEvent { state = state.buttonChangedState(true) }
            .subscribe(
                {
                    events.offer(LogoutEvent)
                }, this::offerErrorEvent
            ).disposeOnCleared()
    }
}

