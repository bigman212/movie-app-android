package ru.redmadrobot.movie_detail

import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.MessageEvent
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.movie_detail.domain.MovieDetailUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val movieDetailUseCase: MovieDetailUseCase
) : BaseViewModel() {

    class ChangeMovieFavoriteStatusEvent(val movieIsFavorite: Boolean) : Event

    sealed class ScreenState {
        data class Content(val data: MovieDetail) : ScreenState()
        object Loading : ScreenState()

        object Empty : ScreenState()

        companion object {
            fun initial(): ScreenState = Loading
        }
    }

    val viewState: MutableLiveData<ScreenState> = MutableLiveData(ScreenState.initial())
    private var state: ScreenState by viewState.delegate()

    fun fetchMovie(id: Int) {
        movieDetailUseCase.movieDetailsById(id)
            .doOnSubscribe { state = ScreenState.Loading }
            .scheduleIoToUi(schedulersProvider)
            .subscribe(
                { movie -> state = ScreenState.Content(movie) },
                { error ->
                    state = ScreenState.Empty
                    Timber.e(error)
                    offerErrorEvent(error)
                }
            ).disposeOnCleared()
    }

    fun onFavoriteButtonClicked(currentMovieId: Int) {
        val oldState = state
        if (oldState is ScreenState.Content) {
            val oldMovieDetail = oldState.data
            val shouldBeAddedToFavorites = !oldMovieDetail.isFavorite

            val addOrRemoveRequest: Completable = if (shouldBeAddedToFavorites) {
                movieDetailUseCase.addMovieToFavorites(currentMovieId)
            } else {
                movieDetailUseCase.removeMovieFromFavorites(currentMovieId)
            }

            addOrRemoveRequest
                .doOnSubscribe { state = ScreenState.Loading }
                .scheduleIoToUi(schedulersProvider)
                .subscribe(
                    {
                        // по завершении запроса меняем статус Избранное у фильма не противоположное
                        val updatedMovieDetail = oldMovieDetail.copy(isFavorite = shouldBeAddedToFavorites)
                        state = oldState.copy(data = updatedMovieDetail)

                        events.offer(MessageEvent("Фильм добавлен в избранное"))
                    },
                    {
                        Timber.e(it)
                        offerErrorEvent(it)
                    }
                ).disposeOnCleared()
        }
    }
}



