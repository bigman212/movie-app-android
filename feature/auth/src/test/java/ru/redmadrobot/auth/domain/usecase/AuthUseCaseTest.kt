package ru.redmadrobot.auth.domain.usecase

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.core.network.SessionIdRepository

internal class AuthUseCaseTest : Spek({
    Feature("Authorize user with UseCase and save session_id") {
        Scenario("successfully authorize user and get session id") {
            val sessionResponse = SessionIdResponse(true, "1235")

            val sessionIdRepository = mock<SessionIdRepository> {
                on { getSessionId() }.doReturn(sessionResponse.sessionId)
            }

            val repo = mock<AuthRepository> {
                on { loginWith(anyString(), anyString()) }.doReturn(Single.just(sessionResponse))
            }

            val authUseCase = AuthUseCase(sessionIdRepository, repo)

            lateinit var request: TestObserver<SessionIdResponse>
            When("it login user with credentials") {
                request = authUseCase.login("login", "password").test()
            }

            Then("it returns session_id response and save it") {
                verify(sessionIdRepository).saveSessionId(sessionResponse.sessionId)
                request
                    .assertNoErrors()
                    .assertValue(sessionResponse)
            }
        }
    }
})
