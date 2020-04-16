package ru.redmadrobot.movie_list.favorite.domain

import io.reactivex.Single
import ru.redmadrobot.common.data.movie.MovieRepository
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.data.profile.AccountRepository
import ru.redmadrobot.core.network.SessionIdRepository
import ru.redmadrobot.core.network.entities.WithPages
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val sessionIdRepository: SessionIdRepository,
    private val movieRepository: MovieRepository
) {

    fun getFavoriteMovies(): Single<List<Movie>> {
        return Single
            .fromCallable(this::createRequestBody)
            .flatMap { accountAndSessionIds ->
                movieRepository.favoriteMovies(accountAndSessionIds.first, accountAndSessionIds.second)
            }
            .map(WithPages<Movie>::results)
    }

    private fun createRequestBody(): Pair<Int, CharSequence> {
        return Pair(accountIdOrException(), sessionIdOrException())
    }

    @Throws(IllegalArgumentException::class)
    private fun accountIdOrException(): Int {
        return accountRepository.currentAccount()
            ?.id
            ?: throw IllegalArgumentException("account_id is null")
    }

    @Throws(IllegalArgumentException::class)
    private fun sessionIdOrException(): CharSequence {
        return sessionIdRepository.getSessionId()
            ?: throw IllegalArgumentException("session_id is null")
    }

}
