package ru.redmadrobot.movie_list.search

import androidx.lifecycle.MutableLiveData
import io.reactivex.subjects.PublishSubject
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.common.extensions.mapDistinct
import ru.redmadrobot.common.vm.ErrorEvent
import ru.redmadrobot.common.vm.EventsQueue
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import timber.log.Timber
import javax.inject.Inject

class MovieListSearchViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val movieRepo: MovieRepository
) : BaseViewModel() {

    private val liveState: MutableLiveData<MovieListSearchViewState> = MutableLiveData(createInitialState())
    private var state: MovieListSearchViewState by liveState.delegate()

    val isFetching = liveState.mapDistinct { it.isFetching }
    val movies = liveState.mapDistinct { it.movies }

    val events = EventsQueue()

    val movieTitlesSubject = PublishSubject.create<CharSequence>()

    fun onSearchMovieInputChanged(movieTitle: CharSequence) {
        if (movieTitle.isBlank()) {
            liveState.postValue(state.noMoviesFoundState())
        } else {
            if (state.isFetching) {
                compositeDisposable.clear() // отменяем все текущие запросы
            }
            movieRepo.findByTitle(movieTitle)
                .scheduleIoToUi(schedulersProvider)
                .subscribe(
                    {
                        state = state.moviesFoundState(it)
                    },
                    {
                        Timber.e(it)
                        events.offer(ErrorEvent(it.message ?: it.toString()))
                    }
                ).disposeOnCleared()
        }
    }

    private fun createInitialState() = MovieListSearchViewState()
}

