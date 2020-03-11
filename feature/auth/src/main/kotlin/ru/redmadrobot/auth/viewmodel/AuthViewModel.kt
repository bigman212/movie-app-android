package ru.redmadrobot.auth.viewmodel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.BiFunction
import ru.redmadrobot.auth.domain.usecase.AuthUseCase
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.ioSubscribe
import ru.redmadrobot.common.extensions.uiObserve
import timber.log.Timber
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val useCase: AuthUseCase) : BaseViewModel() {

    val viewState = MutableLiveData(AuthViewState())

    var loginFieldValue: String = ""
        set(value) {
            field = value
            checkValuesAreValid()
        }

    var passwordFieldValue: String = ""
        set(value) {
            field = value
            checkValuesAreValid()
        }

    private fun checkValuesAreValid() {
        val valuesAreValid = loginFieldValue.isEmail() and passwordFieldValue.isNotBlank()
        dispatch(AuthAction.EnableButton(valuesAreValid))
    }

    private val reducer = BiFunction { previousState: AuthViewState, action: AuthAction ->
        when (action) {
            is AuthAction.Fetching -> previousState.fetchingState()
            is AuthAction.Authorize -> previousState.authorizedState()
            is AuthAction.EnableButton -> previousState.buttonChangedState(action.shouldBeEnabled)

            is AuthAction.Error -> previousState.errorState(action.error)
        }
    }

    private val currentState: AuthViewState
        get() = viewState.value!!

    private fun dispatch(action: AuthAction) =
        reducer.apply(currentState, action)
            .also {
                viewState.postValue(it)
            }

    fun onAuthorizeButtonClick() {
        useCase.login(loginFieldValue, passwordFieldValue)
            .ioSubscribe()
            .uiObserve()
            .doOnSubscribe {
                dispatch(AuthAction.EnableButton(false))
                dispatch(AuthAction.Fetching)
            }
            .subscribe(
                {
                    dispatch(AuthAction.Authorize)
                },
                {
                    Timber.e(it)
                    dispatch(AuthAction.Error(it))
                }
            )
            .disposeOnCleared()
    }

    private fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
}