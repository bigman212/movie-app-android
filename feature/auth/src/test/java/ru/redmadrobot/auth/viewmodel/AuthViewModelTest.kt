package ru.redmadrobot.auth.viewmodel

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import ru.redmadrobot.auth.domain.usecase.AuthUseCase
import ru.redmadrobot.core.network.DefaultResponse
import ru.redmadrobot.core.network.NetworkException
import ru.redmadrobot.core.network.TestSchedulersProvider
import ru.redmadrobot.test_tools.LiveDataExecutionManager

internal class AuthViewModelTest : Spek({
    Feature("AuthViewModel ViewState changes due to user interactions") {
        beforeFeature {
            LiveDataExecutionManager.enableTestMode()
        }

        afterFeature {
            LiveDataExecutionManager.disableTestMode()
        }

        Scenario("enter no login and password or spaces") {
            val authViewModel = AuthViewModel(mock(), mock())

            When("enter blank as credentials") {
                authViewModel.checkValuesAreValid("", "")
            }
            Then("state changes to buttonChangedState = false") {
                val expectedState = AuthViewState().buttonChangedState(false)

                authViewModel.assertStateIs(expectedState)
            }

            When("enter spaces as credentials") {
                authViewModel.checkValuesAreValid("  ", "  ")
            }
            Then("state changes to buttonChangedState = false") {
                val expectedState = AuthViewState().buttonChangedState(false)

                authViewModel.assertStateIs(expectedState)
            }

            When("pass only login credential") {
                authViewModel.checkValuesAreValid("login", "")
            }
            Then("keep state buttonChangedState = false") {
                val expectedState = AuthViewState().buttonChangedState(false)

                authViewModel.assertStateIs(expectedState)
            }

            When("pass only password credential") {
                authViewModel.checkValuesAreValid("login", "")
            }
            Then("keep state buttonChangedState = false") {
                val expectedState = AuthViewState().buttonChangedState(false)

                authViewModel.assertStateIs(expectedState)
            }
        }

        Scenario("enter all credentials") {
            val authViewModel = AuthViewModel(mock(), mock())

            When("pass login and password") {
                authViewModel.checkValuesAreValid("login", "password")
            }
            Then("state changes to buttonChangedState = true") {
                val expectedState = AuthViewState().buttonChangedState(true)

                authViewModel.assertStateIs(expectedState)
            }
        }

        Scenario("authorizing process is started") {
            val testScheduler = TestScheduler()

            val authUseCase = mock<AuthUseCase> {
                on { login(anyString(), anyString()) }.doReturn(Single.just(mock()))
            }
            val authViewModel = AuthViewModel(TestSchedulersProvider(backgroundScheduler = testScheduler), authUseCase)

            When("useCase starts authorizing, but not finishes") {
                authViewModel.checkValuesAreValid("login", "password")
                authViewModel.onAuthorizeButtonClick("login", "password")
            }
            Then("state becomes Fetching") {
                val expectedState = AuthViewState().fetchingState()
                authViewModel.assertStateIs(expectedState)
            }

            When("authorizing process finishes") {
                testScheduler.triggerActions()
            }
            Then("state becomes not Fetching") {
                val expectedState = AuthViewState().authorizedState()
                assertThat(expectedState).isNotEqualTo(authViewModel.viewState.value!!)
            }
        }

        Scenario("enter valid credentials and authorize") {
            val authUseCase = mock<AuthUseCase> {
                on { login(anyString(), anyString()) }.doReturn(Single.just(mock()))
            }
            val authViewModel = AuthViewModel(TestSchedulersProvider(), authUseCase)

            When("valid credentials are entered and button is clicked") {
                val validLogin = "login"
                val validPassword = "password"
                authViewModel.checkValuesAreValid(validLogin, validPassword)
                authViewModel.onAuthorizeButtonClick(validLogin, validPassword)
            }

            Then("state becomes Authorized") {
                val expectedState = AuthViewState()
                    .authorizedState()
                    .buttonChangedState(true)

                authViewModel.assertStateIs(expectedState)
            }
        }

        Scenario("enter wrong credentials and authorize") {
            val expectedWrongCredentialsError = DefaultResponse("Неправильные логин или пароль", 7)

            val authUseCase = mock<AuthUseCase> {
                on { login(anyString(), anyString()) }
                    .doReturn(Single.error { NetworkException.BadRequest(expectedWrongCredentialsError) })
            }

            val authViewModel = AuthViewModel(TestSchedulersProvider(), authUseCase)
            When("wrong credentials are entered and authorize button clicked") {
                val notValidLogin = "login_bad"
                val notValidPassword = "password_bad"
                authViewModel.checkValuesAreValid(notValidLogin, notValidPassword)
                authViewModel.onAuthorizeButtonClick(notValidLogin, notValidPassword)
            }
            Then("state receives error state with invalid_credentials") {
                val expectedState = AuthViewState()
                    .buttonChangedState(isEnabled = true)
                    .errorState(expectedWrongCredentialsError.statusMessage)

                authViewModel.assertStateIs(expectedState)
            }
        }
    }
})

private fun AuthViewModel.assertStateIs(expected: AuthViewState) {
    assertThat(expected).isEqualTo(viewState.value!!)
}

