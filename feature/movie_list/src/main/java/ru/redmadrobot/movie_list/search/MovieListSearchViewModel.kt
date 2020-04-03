package ru.redmadrobot.movie_list.search

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.common.extensions.mapDistinct
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import timber.log.Timber
import javax.inject.Inject

class MovieListSearchViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val movieRepo: MovieRepository
) : BaseViewModel() {

    private val viewState = MutableLiveData(MovieListSearchViewState())
    private var state: MovieListSearchViewState by viewState.delegate()

    val isFetching = viewState.mapDistinct { it.isFetching }
    val movies = viewState.mapDistinct { it.movies }

    fun onSearchMovieInputChanged(movieTitle: CharSequence) {
        if (movieTitle.isBlank()) {
            viewState.postValue(state.noMoviesFoundState())
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
                        offerErrorEvent(it)
                    }
                ).disposeOnCleared()
        }
    }
}

