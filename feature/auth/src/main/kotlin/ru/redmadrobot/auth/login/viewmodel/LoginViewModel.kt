package ru.redmadrobot.auth.login.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.BiFunction
import ru.redmadrobot.auth.login.domain.usecase.LoginUseCase
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.core.network.NetworkException
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel
@Inject constructor(private val schedulers: SchedulersProvider, private val useCase: LoginUseCase) : BaseViewModel() {

    object AuthorizedEvent : Event

    val viewState = MutableLiveData(LoginViewState())

    private val reducer = BiFunction { previousState: LoginViewState, action: LoginAction ->
        when (action) {
            is LoginAction.Fetching -> previousState.fetchingState()
            is LoginAction.Authorize -> {
                events.offer(AuthorizedEvent)
                previousState.authorizedState()
            }
            is LoginAction.EnableButton -> previousState.buttonChangedState(action.shouldBeEnabled)

            is LoginAction.Error -> {
                previousState.errorState(action.uxError)
            }
        }
    }

    private val currentState: LoginViewState
        get() = viewState.value!!

    private fun dispatch(action: LoginAction) =
        reducer.apply(currentState, action)
            .also {
                viewState.postValue(it)
            }

    fun checkValuesAreValid(loginFieldValue: String, passwordFieldValue: String) {
        val valuesAreValid = loginFieldValue.isNotBlank() and passwordFieldValue.isNotBlank()
        dispatch(LoginAction.EnableButton(valuesAreValid))
    }

    fun onAuthorizeButtonClick(loginFieldValue: String, passwordFieldValue: String) {
        useCase.login(loginFieldValue, passwordFieldValue)
            .scheduleIoToUi(schedulers)
            .doOnSubscribe {
                dispatch(LoginAction.EnableButton(false))
                dispatch(LoginAction.Fetching)
            }
            .doOnEvent { _, _ -> dispatch(LoginAction.EnableButton(true)) }
            .subscribe(
                {
                    dispatch(LoginAction.Authorize)
                },
                { error ->
                    Timber.e(error)
                    if (error is NetworkException.NoNetworkConnection) {
                        offerErrorEvent(error)
                    } else {
                        val stateError = error.message ?: "Неизвестная ошибка"
                        dispatch(LoginAction.Error(stateError))
                    }
                }
            )
            .disposeOnCleared()
    }
}
