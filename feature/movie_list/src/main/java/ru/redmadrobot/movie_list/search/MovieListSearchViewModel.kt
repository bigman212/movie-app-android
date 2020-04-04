package ru.redmadrobot.movie_list.search

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.common.extensions.mapDistinct
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.movie_list.Movie
import ru.redmadrobot.movie_list.domain.MovieSearchUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieListSearchViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val searchUseCase: MovieSearchUseCase
) : BaseViewModel() {

    class MovieSearchFinishedEvent(val moviesFound: List<Movie>) : Event

    private val viewState = MutableLiveData(MovieListSearchViewState())
    private var state: MovieListSearchViewState by viewState.delegate()

    val isFetching = viewState.mapDistinct { it.isFetching }

    fun onSearchMovieInputChanged(movieTitle: CharSequence) {
        if (state.isFetching) {
            compositeDisposable.dispose() // отменяем все текущие запросы
        }
        searchUseCase.searchMovie(movieTitle)
            .doOnSubscribe { state = state.fetchingState() }
            .scheduleIoToUi(schedulersProvider)
            .doOnEvent { _, _ -> state = state.fetchingFinishedState() }
            .subscribe(
                {
                    events.offer(MovieSearchFinishedEvent(it))
                },
                {
                    Timber.e(it)
                    offerErrorEvent(it)
                }
            ).disposeOnCleared()
    }
}

