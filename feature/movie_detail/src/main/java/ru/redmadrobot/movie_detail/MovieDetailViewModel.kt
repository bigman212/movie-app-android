package ru.redmadrobot.movie_detail

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.data.movie.MovieRepository
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import timber.log.Timber
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val movieRepo: MovieRepository
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
        movieRepo.movieDetailsById(id)
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
}



