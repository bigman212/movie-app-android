package ru.redmadrobot.auth.login.domain.usecase

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
    Feature("create authorized session (login user)") {
        Scenario("create authorized session") {
            val session = SessionIdResponse(true, "1235")

            val sessionIdRepository = mock<SessionIdRepository> {
                on { getSessionId() }.doReturn(session.sessionId)
            }

            val repo = mock<AuthRepository> {
                on { loginWith(anyString(), anyString()) }.doReturn(Single.just(session))
            }

            val authUseCase = LoginUseCase(sessionIdRepository, repo)

            lateinit var sessionRequest: TestObserver<SessionIdResponse>
            When("pass valid login and password") {
                sessionRequest = authUseCase.login("login", "password").test()
            }
            Then("login and receive new session") {
                sessionRequest
                    .assertNoErrors()
                    .assertComplete()
                    .assertValue(session)
            }
            And("session saved in repository") {
                verify(sessionIdRepository).saveSessionId(session.sessionId)
            }
        }
    }
})
