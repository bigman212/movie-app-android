package ru.redmadrobot.auth.viewmodel

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import ru.redmadrobot.core.network.ErrorResponse
import ru.redmadrobot.core.network.NetworkException
import ru.redmadrobot.core.network.TestSchedulersProvider
import ru.redmadrobot.test_tools.LiveDataExecutionManager
import kotlin.test.assertEquals


internal class AuthViewModelTest : Spek({
    Feature("AuthViewModel state") {
        beforeFeature {
            LiveDataExecutionManager.enableTestMode()
        }

        afterFeature {
            LiveDataExecutionManager.disableTestMode()
        }

        Scenario("enter different values of credentials") {
            val authViewModel = AuthViewModel(mock(), mock())

            When("enter blank as credentials") {
                authViewModel.checkValuesAreValid("", "")
            }
            Then("state changes to buttonChangedState = false") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = currentState.buttonChangedState(false)

                assertEquals(expectedState, currentState)
            }

            When("pass only one credential") {
                authViewModel.checkValuesAreValid("login", "")
            }
            Then("state keeps state buttonChangedState = false") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = AuthViewState().buttonChangedState(false)

                assertEquals(expectedState, currentState)
            }

            When("pass all credentials") {
                authViewModel.checkValuesAreValid("login", "password")
            }
            Then("state changes to buttonChangedState = true") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = AuthViewState().buttonChangedState(true)

                assertEquals(expectedState, currentState)
            }
        }

        Scenario("authorize user with valid credentials") {
            val testScheduler = TestScheduler()
            val authViewModel = AuthViewModel(TestSchedulersProvider(backgroundScheduler = testScheduler), mock {
                on { login(anyString(), anyString()) }.doReturn(Single.just(mock()))
            })

            val observer: Observer<AuthViewState> = mock {}
            When("valid credentials are entered and button is clicked") {
                authViewModel.viewState.observeForever(observer)
                authViewModel.checkValuesAreValid("login", "password")
                authViewModel.onAuthorizeButtonClick("login", "password")
                testScheduler.triggerActions()
            }
            Then("state becomes Authorized") {
                val expectedState = AuthViewState().authorizedState()
                    .buttonChangedState(true)

                authViewModel.assertStateIs(expectedState)
            }
            And("state changes accordingly in order") {
                inOrder(observer) {
                    with(verify(observer)) {
                        val defaultViewState = AuthViewState()
                        val buttonEnabledState = defaultViewState.buttonChangedState(isEnabled = true)
                        val requestButtonDisabledState = buttonEnabledState.buttonChangedState(isEnabled = false)
                        val requestIsFetchingState = requestButtonDisabledState.fetchingState()
                        val requestIsFinishedSuccessfullyState = requestIsFetchingState.authorizedState()

                        onChanged(buttonEnabledState)
                        onChanged(requestButtonDisabledState)
                        onChanged(requestIsFetchingState)
                        onChanged(requestIsFinishedSuccessfullyState)
                    }
                }
                authViewModel.viewState.removeObserver(observer)
            }
        }

        Scenario("authorize user with wrong credentials") {
            val expectedWrongCredentialsError = ErrorResponse("Неправильные логин или пароль", 7)

            val testScheduler = TestScheduler()
            val authViewModel = AuthViewModel(TestSchedulersProvider(backgroundScheduler = testScheduler), mock {
                on { login(anyString(), anyString()) }
                    .doReturn(Single.error { NetworkException.BadRequest(expectedWrongCredentialsError) })
            })

            val observer: Observer<AuthViewState> = mock {}
            When("wrong credentials are entered and authorize button clicked") {
                authViewModel.viewState.observeForever(observer)

                authViewModel.checkValuesAreValid("bad_login", "bad_password")
                authViewModel.onAuthorizeButtonClick("bad_login", "bad_login")

                testScheduler.triggerActions()
            }
            Then("state receives error state with invalid_credentials") {
                val expectedState = AuthViewState()
                    .buttonChangedState(isEnabled = true)
                    .errorState(expectedWrongCredentialsError.statusMessage)

                authViewModel.assertStateIs(expectedState)
            }
            And("state changes in order") {
                inOrder(observer) {
                    with(verify(observer)) {
                        val defaultViewState = AuthViewState()
                        val buttonEnabledState = defaultViewState.buttonChangedState(isEnabled = true)
                        val requestButtonDisabledState = buttonEnabledState.buttonChangedState(isEnabled = false)
                        val requestIsFetchingState = requestButtonDisabledState.fetchingState()
                        val requestFinishedWithErrorState =
                            requestIsFetchingState.errorState(expectedWrongCredentialsError.statusMessage)

                        onChanged(buttonEnabledState)
                        onChanged(requestButtonDisabledState)
                        onChanged(requestIsFetchingState)
                        onChanged(requestFinishedWithErrorState)
                        onChanged(requestFinishedWithErrorState.buttonChangedState(true))
                    }
                }
                authViewModel.viewState.removeObserver(observer)
            }
        }
    }
})

private fun AuthViewModel.assertStateIs(expected: AuthViewState) {
    assertThat(expected).isEqualTo(viewState.value!!)
}
