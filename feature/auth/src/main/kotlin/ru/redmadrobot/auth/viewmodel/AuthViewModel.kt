package ru.redmadrobot.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import ru.redmadrobot.auth.domain.usecase.AuthUseCase
import javax.inject.Inject


class AuthViewModel @Inject constructor(private val useCase: AuthUseCase) : ViewModel() {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private val reducer = BiFunction { previousState: AuthViewState, action: AuthAction ->
        when (action) {
            is AuthAction.Fetching -> previousState.fetchingState()
            is AuthAction.Authorize -> previousState.authorizedState()
            is AuthAction.WrongCredentialsError -> previousState.wrongCredentialsErrorState()

            is AuthAction.UnknownError -> previousState.unknownErrorState(action.unknownError)
        }
    }

    private val _viewState = MutableLiveData(AuthViewState())

    private val currentState: AuthViewState
        get() = _viewState.value!!

    fun viewState(): LiveData<AuthViewState> = _viewState

    private fun dispatch(action: AuthAction) =
        reducer.apply(currentState, action)
            .also {
                _viewState.postValue(it)
            }


    fun onAuthorizeButtonClick(login: String, password: String) {
        compositeDisposable.add(
            useCase.login(login, password)
                .doOnSubscribe {
                    dispatch(AuthAction.Fetching)
                }
                .subscribe(
                    {
                        if (it.authorized) dispatch(AuthAction.Authorize)
                        else dispatch(AuthAction.WrongCredentialsError)
                    },
                    {
                        dispatch(AuthAction.UnknownError(it))
                    }
                )
        )
    }
}