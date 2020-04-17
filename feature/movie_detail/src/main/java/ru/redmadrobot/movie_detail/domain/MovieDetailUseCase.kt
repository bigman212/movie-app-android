package ru.redmadrobot.movie_detail.domain

import io.reactivex.Completable
import io.reactivex.Single
import ru.redmadrobot.common.data.movie.MovieRepository
import ru.redmadrobot.common.data.movie.entity.MarkMovieFavoriteRequest
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.common.data.profile.AccountRepository
import ru.redmadrobot.core.network.SessionIdRepository
import javax.inject.Inject

class MovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val sessionIdRepository: SessionIdRepository,
    private val accountRepository: AccountRepository
) {

    fun movieDetailsById(movieId: Int): Single<MovieDetail> {
        return movieRepository.movieDetailsById(movieId, getSessionId())
    }

    fun addMovieToFavorites(movieId: Int): Completable = markMovieAsFavorite(movieId, markFavorite = true)

    fun removeMovieFromFavorites(movieId: Int): Completable = markMovieAsFavorite(movieId, markFavorite = false)

    private fun markMovieAsFavorite(movieId: Int, markFavorite: Boolean): Completable {
        return try {
            val accountId = getAccountId()
            val sessionId = getSessionId()

            val body = MarkMediaFavoriteRequest(movieId, isFavorite = markFavorite)
            movieRepository
                .markMovieAsFavorite(accountId, sessionId, body)
                .flatMapCompletable { Completable.complete() }
        } catch (someNull: IllegalArgumentException) {
            Completable.error(someNull)
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getAccountId(): Int {
        return accountRepository.currentAccount()?.id ?: throw IllegalArgumentException("Account is null")
    }

    @Throws(IllegalArgumentException::class)
    private fun getSessionId(): String {
        return sessionIdRepository.getSessionId() ?: throw IllegalArgumentException("session_id is null")
    }

}
