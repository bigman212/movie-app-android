package ru.redmadrobot.auth.viewmodel

import android.annotation.SuppressLint
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.BiFunction
import ru.redmadrobot.auth.domain.usecase.AuthUseCase
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.ioSubscribe
import ru.redmadrobot.common.extensions.uiObserve
import javax.inject.Inject

class AuthViewModel
@Inject constructor(private val useCase: AuthUseCase) : BaseViewModel() {

    val viewState = MutableLiveData(AuthViewState())

    private val reducer = BiFunction { previousState: AuthViewState, action: AuthAction ->
        when (action) {
            is AuthAction.Fetching -> previousState.fetchingState()
            is AuthAction.Authorize -> previousState.authorizedState()
            is AuthAction.EnableButton -> previousState.buttonChangedState(action.shouldBeEnabled)

            is AuthAction.Error -> previousState.errorState(action.uxError)
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
            .flatMap { useCase.popularTvShows() }
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
                    dispatch(AuthAction.Error(it.message))
                }
            )
            .disposeOnCleared()
    }

    @SuppressLint("NewApi")
    private fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
