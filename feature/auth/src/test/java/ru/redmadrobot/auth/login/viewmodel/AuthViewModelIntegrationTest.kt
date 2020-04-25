package ru.redmadrobot.auth.login.viewmodel

import android.content.Context
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import ru.redmadrobot.auth.data.AuthApi
import ru.redmadrobot.auth.data.entities.response.AccountAvatar
import ru.redmadrobot.auth.data.entities.response.AccountDetailsResponse
import ru.redmadrobot.auth.data.entities.response.Gravatar
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.entities.response.TokenResponse
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.auth.login.domain.usecase.LoginUseCase
import ru.redmadrobot.common.data.genre.GenreApi
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.common.data.profile.AccountRepository
import ru.redmadrobot.core.network.DefaultResponse
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

    defaultTimeout = 5000000

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
    val networkEnvironment = NetworkEnvironment(errorInterceptor)

    val authRepository = AuthRepository(networkEnvironment.createApi(AuthApi::class))

    val sharedPrefs = SharedPreferencesMock(mapOf("session_id_key" to "session_id"))

    val sessionIdRepo = SessionIdRepository(sharedPrefs.sharedPrefs)

    val genresRepo = GenresRepository(mock(), networkEnvironment.createApi(GenreApi::class))
    val accountRepo = AccountRepository(moshi, sharedPrefs.sharedPrefs)

    val authUseCase = LoginUseCase(authRepository, sessionIdRepo, genresRepo, accountRepo)
    //endregion

    Feature("Login") {
        defaultTimeout = 5000000
        beforeFeature {
            LiveDataExecutionManager.enableTestMode()
        }

        afterFeature {
            LiveDataExecutionManager.disableTestMode()
        }

        val authViewModel by memoized {
            LoginViewModel(TestSchedulersProvider(), authUseCase)
        }

        Scenario("authorize user with entered credentials") {
            defaultTimeout = 5000000
            networkEnvironment.dispatchResponses { url ->
                if (NetworkRouter.ACCOUNT_DETAILS in url) {
                    return@dispatchResponses MockResponse()
                        .success(
                            moshi.toJson(
                                AccountDetailsResponse(
                                    AccountAvatar(Gravatar("hash_string")),
                                    id = 1,
                                    iso639_1 = "iso639",
                                    iso3166_1 = "iso3166",
                                    name = "bigman",
                                    shouldIncludeAdult = true,
                                    username = "username"
                                )
                            )
                        )
                }

                when (url) {
                    NetworkRouter.AUTH_TOKEN_NEW, NetworkRouter.AUTH_VALIDATE_TOKEN -> MockResponse()
                        .success(
                            moshi.toJson(
                                TokenResponse(true, "10.10.2020", "request_token")
                            )
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
                val errorResponse = DefaultResponse("invalid_credentials", 30)
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
