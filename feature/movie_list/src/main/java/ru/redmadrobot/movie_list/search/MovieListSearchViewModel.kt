package ru.redmadrobot.movie_list.search

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.common.extensions.mapDistinct
import ru.redmadrobot.common.extensions.uiObserve
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.movie_list.data.entity.Movie
import ru.redmadrobot.movie_list.data.entity.MovieDetail
import ru.redmadrobot.movie_list.domain.MovieSearchUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieListSearchViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val searchUseCase: MovieSearchUseCase
) : BaseViewModel() {

    class MovieSearchFinishedEvent(val moviesFound: List<Movie>) : Event
    class MovieSearchRuntimeFetchedEvent(val fetchedMovie: MovieDetail) : Event

    private val viewState = MutableLiveData(MovieListSearchViewState())
    private var state: MovieListSearchViewState by viewState.delegate()

    val isFetching = viewState.mapDistinct { it.isFetching }

    fun onSearchMovieInputChanged(movieTitle: CharSequence) {
        if (state.isFetching) {
            compositeDisposable.dispose() // отменяем все текущие запросы
        }
        searchUseCase.searchMovie(movieTitle)
            .doOnSubscribe { state = state.fetchingState() }
            .doOnEvent { _, _ -> state = state.fetchingFinishedState() }
            .uiObserve()
            .doOnSuccess { events.offer(MovieSearchFinishedEvent(it)) }
            .observeOn(schedulersProvider.io())
            .flattenAsObservable { it }
            .flatMap {
                searchUseCase.fetchMovieDetails(it.id)
//                    .onErrorResumeNext(Observable.empty()) // при возникновении ошибки пропускаем и идем альше
            }
            .scheduleIoToUi(schedulersProvider)
            .subscribe(
                {
                    events.offer(MovieSearchRuntimeFetchedEvent(it))
                },
                {
                    Timber.e(it)
                    offerErrorEvent(it)
                }
            ).disposeOnCleared()
    }
}

private fun <T> Observable<T>.scheduleIoToUi(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.io()).observeOn(schedulers.ui())
}

