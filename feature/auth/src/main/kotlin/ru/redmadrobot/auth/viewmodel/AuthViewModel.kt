package ru.redmadrobot.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.BiFunction
import ru.redmadrobot.auth.domain.usecase.AuthUseCase
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.core.network.NetworkException
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import javax.inject.Inject

class AuthViewModel
@Inject constructor(private val schedulers: SchedulersProvider, private val useCase: AuthUseCase) : BaseViewModel() {

    object AuthorizedEvent : Event

    val viewState = MutableLiveData(AuthViewState())

    private val reducer = BiFunction { previousState: AuthViewState, action: AuthAction ->
        when (action) {
            is AuthAction.Fetching -> previousState.fetchingState()
            is AuthAction.Authorize -> {
                events.offer(AuthorizedEvent)
                previousState.authorizedState()
            }
            is AuthAction.EnableButton -> previousState.buttonChangedState(action.shouldBeEnabled)

            is AuthAction.Error -> {
                previousState.errorState(action.uxError)
            }
        }
    }

    private val currentState: AuthViewState
        get() = viewState.value!!

    private fun dispatch(action: AuthAction) =
        reducer.apply(currentState, action)
            .also {
                viewState.postValue(it)
            }

    fun checkValuesAreValid(loginFieldValue: String, passwordFieldValue: String) {
        val valuesAreValid = loginFieldValue.isNotBlank() and passwordFieldValue.isNotBlank()
        dispatch(AuthAction.EnableButton(valuesAreValid))
    }

    fun onAuthorizeButtonClick(loginFieldValue: String, passwordFieldValue: String) {
        useCase.login(loginFieldValue, passwordFieldValue)
            .scheduleIoToUi(schedulers)
            .doOnSubscribe {
                dispatch(AuthAction.EnableButton(false))
                dispatch(AuthAction.Fetching)
            }
            .doOnEvent { _, _ -> dispatch(AuthAction.EnableButton(true)) }
            .subscribe(
                {
                    dispatch(AuthAction.Authorize)
                },
                {
                    if (it is NetworkException.NoNetworkConnection) {
                        offerErrorEvent(it)
                    } else {
                        val stateError = it.message ?: "Неизвестная ошибка"
                        dispatch(AuthAction.Error(stateError))
                    }
                }
            )
            .disposeOnCleared()
    }
}
