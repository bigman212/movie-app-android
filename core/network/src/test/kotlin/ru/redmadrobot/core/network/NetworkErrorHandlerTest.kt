package ru.redmadrobot.core.network

import android.content.Context
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import ru.redmadrobot.test_tools.network.makeGet
import java.util.concurrent.TimeUnit

object NetworkErrorHandlerTest : Spek({
    Feature("Handle network errors") {
        val dispatcher: Dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockErrorResponseCreator.fromUrl(request.path!!)
            }
        }

        val context = mock<Context> {
            on { getString(R.string.network_error_invalid_api_key) } doReturn "Неправильный апи ключ"
            on { getString(R.string.network_error_unknown) } doReturn "Неизвестная ошибка"
            on { getString(R.string.network_error_session_denied) } doReturn "Сессия просрочена или неправильная"
            on { getString(R.string.network_invalid_credentials) } doReturn "Неправильный логин или пароль"
        }

        val server = MockWebServer().apply {
            this.dispatcher = dispatcher
            start()
        }
        val httpUrl = server.url("").toString().removeSuffix("/")

        val moshi = Moshi.Builder().build()
        val networkErrorHandler = NetworkErrorHandler(context, moshi)

        lateinit var response: Response
        lateinit var errorResponseThrowable: NetworkException

        Scenario("make GET to new request_token but without api_key") {
            Given("response from getting new request_token") {
                val apiClient = OkHttpClient.Builder()
                    .readTimeout(100, TimeUnit.HOURS)
                    .build()
                response = apiClient.makeGet(httpUrl + NetworkRouter.AUTH_TOKEN_NEW)
            }
            When("it handles error response") {
                errorResponseThrowable = networkErrorHandler.networkExceptionToThrow(response)
            }
            Then("it should return BadRequest error with context string of invalid api key") {
                assertSoftly {
                    it.assertThat(errorResponseThrowable)
                        .isInstanceOf(NetworkException.BadRequest::class.java)
                    it.assertThat(errorResponseThrowable.message)
                        .isEqualTo(context.getString(R.string.network_error_invalid_api_key))
                }
            }
        }
    }
})
