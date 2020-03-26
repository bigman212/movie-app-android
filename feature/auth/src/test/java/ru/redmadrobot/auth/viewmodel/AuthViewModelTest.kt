package ru.redmadrobot.auth.viewmodel

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockResponse
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import ru.redmadrobot.auth.data.AuthService
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.entities.response.TokenResponse
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.auth.domain.usecase.AuthUseCase
import ru.redmadrobot.core.network.*
import ru.redmadrobot.core.network.interceptors.ErrorInterceptor
import ru.redmadrobot.test_tools.LiveDataExecutionManager
import ru.redmadrobot.test_tools.network.NetworkEnvironment
import ru.redmadrobot.test_tools.network.badRequest
import ru.redmadrobot.test_tools.network.notFound
import ru.redmadrobot.test_tools.network.success
import kotlin.test.assertEquals


internal class AuthViewModelTest : Spek({

    val networkErrorHandler = mock<NetworkErrorHandler> {
        on { networkExceptionToThrow(any()) }.doReturn(
            NetworkException.BadRequest(ErrorResponse("Неправильный логин или пароль", 7))
        )
    }
    val networkEnvironment = NetworkEnvironment(AuthService::class, ErrorInterceptor(networkErrorHandler, mock()))
    Feature("Login") {
        beforeFeature {
            LiveDataExecutionManager.enableTestMode()
        }

        afterFeature {
            LiveDataExecutionManager.disableTestMode()
        }

        val authRepository = AuthRepository(networkEnvironment.api)
        val authViewModel = AuthViewModel(TestSchedulersProvider(), AuthUseCase(mock(), authRepository))

        Scenario("enter credentials and click login button and authorize successfully") {
            networkEnvironment.dispatchResponses {
                val moshi = Moshi.Builder().build()
                when (it) {
                    NetworkRouter.AUTH_TOKEN_NEW, NetworkRouter.AUTH_VALIDATE_TOKEN -> MockResponse()
                        .success(
                            moshi.toJson(TokenResponse(true, "10.10.2020", "request_token"))
                        )
                    NetworkRouter.AUTH_CREATE_SESSION_ID -> MockResponse()
                        .success(
                            moshi.toJson(
                                SessionIdResponse(true, "session_id")
                            )
                        )
                    else -> MockResponse().notFound()
                }
            }

            When("enter not blank login and password") {
                authViewModel.checkValuesAreValid("login", "password")

                val currentState = authViewModel.viewState.value
                val expectedState = AuthViewState().buttonChangedState(true)
                assertEquals(expectedState, currentState)
            }
            And("click login button") {
                authViewModel.onAuthorizeButtonClick("login", "password")
            }
            Then("state should be in authorized mode") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = currentState.authorizedState()
                assertEquals(expectedState, currentState)
            }
        }

        Scenario("!!! DOESN'T WORK !!! try to login and fail with invalid_credentials error") {

            Given("invalid_credentials error from server") {
                val moshi = Moshi.Builder().build()
                networkEnvironment.dispatchResponses { path ->
                    when (path) {

                        NetworkRouter.AUTH_TOKEN_NEW -> MockResponse()
                            .success(
                                moshi.toJson(TokenResponse(true, "10.10.2020", "request_token"))
                            )
                        NetworkRouter.AUTH_CREATE_SESSION_ID -> MockResponse()
                            .success(
                                moshi.toJson(
                                    SessionIdResponse(true, "session_id")
                                )
                            )
                        NetworkRouter.AUTH_VALIDATE_TOKEN -> MockResponse()
                            .badRequest(moshi.toJson(ErrorResponse("invalid", 8)))
                        else -> MockResponse().notFound()
                    }
                }
            }

            When("pass wrong login and password to viewmodel") {
                authViewModel.checkValuesAreValid("login1", "password1")
            }
            And("click button to authrorize") {
                authViewModel.onAuthorizeButtonClick("login1", "password1")
            }
            Then("recieve invalid_credentials error and keep it in the state") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = currentState.errorState("error")
                assertEquals(expectedState, currentState)
            }
        }

        Scenario("credentials are blank and button state should be disabled") {
            When("pass nothing in viewmodel") {
                authViewModel.checkValuesAreValid("", "")
            }
            Then("state should be not changed with button enabled = false") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = currentState.buttonChangedState(false)

                assertEquals(expectedState, currentState)
            }
        }

        Scenario("one of credentials is blank and button state should be disabled") {
            When("pass only login value to viewmodel") {
                authViewModel.checkValuesAreValid("login", "")
            }
            Then("state should be not changed and with button enabled = false") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = currentState.buttonChangedState(false)

                assertEquals(expectedState, currentState)
            }
        }
    }
})

private inline fun <reified T> Moshi.toJson(entity: T): String = this.adapter(T::class.java).toJson(entity)
