package ru.redmadrobot.movie_detail.domain

import io.reactivex.Completable
import io.reactivex.Single
import ru.redmadrobot.common.data.movie.FavoriteMovieRepository
import ru.redmadrobot.common.data.movie.MovieRepository
import ru.redmadrobot.common.data.movie.entity.MarkMediaFavoriteRequest
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.common.data.profile.AccountRepository
import ru.redmadrobot.core.network.SessionIdRepository
import timber.log.Timber
import javax.inject.Inject

class MovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoriteMovieRepository: FavoriteMovieRepository,
    private val sessionIdRepository: SessionIdRepository,
    private val accountRepository: AccountRepository
) {

    fun movieDetailsById(movieId: Long): Single<MovieDetail> {
        return movieRepository.movieDetailsById(movieId)
            .flatMap { fetchedMovieDetail ->
                favoriteMovieRepository
                    .isMovieInFavorites(movieId)
                    .doOnError { Timber.e(it) }
                    .map { fetchedMovieDetail.copy(isFavorite = it) }
                    .onErrorReturnItem(fetchedMovieDetail)
            }
    }

    fun addMovieToFavorites(movieId: Long): Completable = markMovieAsFavorite(movieId, markFavorite = true)

    fun removeMovieFromFavorites(movieId: Long): Completable = markMovieAsFavorite(movieId, markFavorite = false)

    private fun markMovieAsFavorite(movieId: Long, markFavorite: Boolean): Completable {
        return try {
            val accountId = getAccountId()
            val sessionId = getSessionId()

            val body = MarkMediaFavoriteRequest(movieId, isFavorite = markFavorite)

            favoriteMovieRepository
                .markMovieAsFavorite(accountId, sessionId, body)
                .flatMapCompletable { Completable.complete() }
        } catch (someOfVarsAreNull: IllegalArgumentException) {
            Completable.error(someOfVarsAreNull)
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getAccountId(): Long {
        return accountRepository.currentAccount()?.id ?: throw IllegalArgumentException("Account is null")
    }

    @Throws(IllegalArgumentException::class)
    private fun getSessionId(): String {
        return sessionIdRepository.getSessionId() ?: throw IllegalArgumentException("session_id is null")
    }

}
