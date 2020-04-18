package ru.redmadrobot.profile

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.auth.login.LoginFragmentDirections
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.common.vm.NavigateToEvent
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.profile.data.ProfileUseCase
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val profileRepo: ProfileUseCase
) : BaseViewModel() {

    val viewState = MutableLiveData(ProfileViewState())
    private var state: ProfileViewState by viewState.delegate()

    fun loadAccountDetails() {
        profileRepo.getAccountDetails()
            .subscribe(
                {
                    state = state.withAccountDetails(it)
                }, {
                    state = state.withAccountDetails(null)
                    Timber.e(it)
                }
            ).disposeOnCleared()
    }

    fun onLogoutButtonClicked() {
        profileRepo.logout()
            .doOnSubscribe { state = state.fetchingState() }
            .scheduleIoToUi(schedulersProvider)
            .doOnEvent { state = state.fetchingFinishedState() }
            .subscribe(
                {
                    events.offer(NavigateToEvent(LoginFragmentDirections.toLoginFragmentGlobal()))
                },
                this::offerErrorEvent
            ).disposeOnCleared()
    }
}

