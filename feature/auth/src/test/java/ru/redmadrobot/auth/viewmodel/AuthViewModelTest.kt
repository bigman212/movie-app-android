package ru.redmadrobot.auth.viewmodel

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
import ru.redmadrobot.common.LiveDataExecutionManager
import ru.redmadrobot.common.TestSchedulersProvider
import ru.redmadrobot.core.network.NetworkEnvironment
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.core.network.notFound
import ru.redmadrobot.core.network.success
import javax.net.ssl.HttpsURLConnection
import kotlin.test.assertEquals


internal class AuthViewModelTest : Spek({
    val networkEnvironment = NetworkEnvironment(AuthService::class)

    Feature("Success login") {
        beforeFeature {
            LiveDataExecutionManager.enableTestMode()
        }

        afterFeature {
            LiveDataExecutionManager.disableTestMode()
        }

        Scenario("enter credentials and click login button and authorize successfully") {
            networkEnvironment.dispatchResponses {
                val moshi = Moshi.Builder().build()
                when (it) {
                    NetworkRouter.AUTH_TOKEN_NEW, NetworkRouter.AUTH_VALIDATE_TOKEN -> MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
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

            val authRepository = AuthRepository(networkEnvironment.api)
            val authViewModel = AuthViewModel(TestSchedulersProvider(), AuthUseCase(mock(), authRepository))
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
    }
})

private inline fun <reified T> Moshi.toJson(entity: T): String = this.adapter(T::class.java).toJson(entity)
