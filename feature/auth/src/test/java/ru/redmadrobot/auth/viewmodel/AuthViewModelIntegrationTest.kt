package ru.redmadrobot.auth.viewmodel

import android.content.Context
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import ru.redmadrobot.auth.data.AuthService
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.entities.response.TokenResponse
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.auth.domain.usecase.AuthUseCase
import ru.redmadrobot.core.network.ErrorResponse
import ru.redmadrobot.core.network.NetworkErrorHandler
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.core.network.R
import ru.redmadrobot.core.network.SessionIdRepository
import ru.redmadrobot.core.network.TestSchedulersProvider
import ru.redmadrobot.core.network.interceptors.ErrorInterceptor
import ru.redmadrobot.test_tools.LiveDataExecutionManager
import ru.redmadrobot.test_tools.android.SharedPreferencesMock
import ru.redmadrobot.test_tools.network.NetworkEnvironment
import ru.redmadrobot.test_tools.network.badRequest
import ru.redmadrobot.test_tools.network.notFound
import ru.redmadrobot.test_tools.network.success

// моки ближе к сценарию в when чтобы возвралось ожидаемое поведение
internal class AuthViewModelIntegrationTest : Spek({

    //region init global vars
    val context = mock<Context> {
        on { getString(R.string.network_error_invalid_api_key) } doReturn "Неправильный апи ключ"
        on { getString(R.string.network_error_unknown) } doReturn "Неизвестная ошибка"
        on { getString(R.string.network_error_session_denied) } doReturn "Сессия просрочена или неправильная"
        on { getString(R.string.network_invalid_credentials) } doReturn "Неправильный логин или пароль"
    }

    val moshi = Moshi.Builder().build()
    val networkErrorHandler = NetworkErrorHandler(context, moshi)

    val errorInterceptor = ErrorInterceptor(networkErrorHandler, mock())
    val networkEnvironment = NetworkEnvironment(AuthService::class, errorInterceptor)

    val authRepository = AuthRepository(networkEnvironment.api)

    val sessionIdRepo = SessionIdRepository(
        SharedPreferencesMock(mapOf("session_id_key" to "session_id")).sharedPrefs
    )
    val authUseCase = AuthUseCase(sessionIdRepo, authRepository)
    //endregion

    Feature("Login") {
        beforeFeature {
            LiveDataExecutionManager.enableTestMode()
        }

        afterFeature {
            LiveDataExecutionManager.disableTestMode()
        }

        val authViewModel by memoized {
            AuthViewModel(TestSchedulersProvider(), authUseCase)
        }

        Scenario("authorize user with entered credentials") {
            networkEnvironment.dispatchResponses {
                when (it) {
                    NetworkRouter.AUTH_TOKEN_NEW, NetworkRouter.AUTH_VALIDATE_TOKEN -> MockResponse()
                        .success(
                            moshi.toJson(TokenResponse(true, "10.10.2020", "request_token"))
                        )
                    NetworkRouter.AUTH_CREATE_SESSION_ID -> MockResponse()
                        .success(
                            moshi.toJson(SessionIdResponse(true, "session_id"))
                        )
                    else -> MockResponse().notFound()
                }
            }

            val loginValue = "login"
            val passwordValue = "password"
            When("enter valid login and password") {
                authViewModel.checkValuesAreValid(loginValue, passwordValue)
            }
            And("click login button") {
                authViewModel.onAuthorizeButtonClick(loginValue, passwordValue)
            }
            Then("user is authorized") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = currentState.authorizedState()

                assertThat(expectedState).isEqualTo(currentState)
            }
        }

        Scenario("fail to authorize user with entered credentials") {
            Given("invalid_credentials error from server") {
                val errorResponse = ErrorResponse("invalid_credentials", 30)
                networkEnvironment.dispatchResponses { path ->
                    when (path) {
                        NetworkRouter.AUTH_TOKEN_NEW -> MockResponse()
                            .success(moshi.toJson(TokenResponse(true, "10.10.2020", "request_token")))
                        NetworkRouter.AUTH_VALIDATE_TOKEN -> MockResponse()
                            .badRequest(moshi.toJson(errorResponse))
                        else -> MockResponse().notFound()
                    }
                }
            }

            val wrongLoginValue = "login_2"
            val wrongPasswordValue = "password_2"
            When("enter wrong login and password") {
                authViewModel.checkValuesAreValid(wrongLoginValue, wrongPasswordValue)
            }
            And("click login button") {
                authViewModel.onAuthorizeButtonClick(wrongLoginValue, wrongPasswordValue)
            }
            Then("receive invalid_credentials error") {
                val currentState = authViewModel.viewState.value!!
                val expectedState = currentState.errorState(context.getString(R.string.network_invalid_credentials))
                assertThat(expectedState).isEqualTo(currentState)
            }
        }
    }
})

private inline fun <reified T> Moshi.toJson(entity: T): String = this.adapter(T::class.java).toJson(entity)
