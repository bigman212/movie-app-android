package ru.redmadrobot.auth.login.domain.usecase

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import ru.redmadrobot.auth.data.entities.response.AccountAvatar
import ru.redmadrobot.auth.data.entities.response.AccountDetailsResponse
import ru.redmadrobot.auth.data.entities.response.Gravatar
import ru.redmadrobot.auth.data.entities.response.SessionIdResponse
import ru.redmadrobot.auth.data.repository.AuthRepository
import ru.redmadrobot.common.data.genre.GenresRepository
import ru.redmadrobot.common.data.profile.AccountRepository
import ru.redmadrobot.core.network.SessionIdRepository

internal class AuthUseCaseTest : Spek({
    Feature("create authorized session (login user)") {
        Scenario("create authorized session") {
            val session = SessionIdResponse(true, "1235")
            val account = AccountDetailsResponse(
                avatar = AccountAvatar(Gravatar("hash_string")),
                id = 1,
                iso639_1 = "iso639",
                iso3166_1 = "iso3166",
                name = "bigman",
                shouldIncludeAdult = true,
                username = "username"
            )

            val sessionIdRepo = mock<SessionIdRepository>()

            val authRepo = mock<AuthRepository> {
                on { loginWith(anyString(), anyString()) }.doReturn(Single.just(session))
                on { fetchAccountDetails(eq(session.sessionId)) }.doReturn(Single.just(account))
            }

            val genresRepo = mock<GenresRepository> {
                on { fetchGenresAndSave() }.doReturn(Completable.complete())
            }

            val accountRepo = mock<AccountRepository>()

            val authUseCase = LoginUseCase(
                authRepo = authRepo,
                sessionRepo = sessionIdRepo,
                genresRepo = genresRepo,
                accountRepo = accountRepo
            )

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
                verify(sessionIdRepo).saveSessionId(session.sessionId)
            }

            And("genres and account info saved") {
                verify(genresRepo).fetchGenresAndSave()
                verify(accountRepo).save(any())
            }
        }
    }
})
