package ru.redmadrobot.movie_detail

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.common.extensions.delegate
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
        movieDetailUseCase.markMovieFavorite(currentMovieId)
            .scheduleIoToUi(schedulersProvider)
            .subscribe(
                { events.offer(MessageEvent("Фильм добавлен в избранное")) },
                {
                    Timber.e(it)
                    offerErrorEvent(it)
                }
            ).disposeOnCleared()
    }
}



