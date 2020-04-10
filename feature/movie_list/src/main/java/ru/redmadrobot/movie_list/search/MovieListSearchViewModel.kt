package ru.redmadrobot.movie_list.search

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.movie_list.search.domain.MovieSearchUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieListSearchViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val searchUseCase: MovieSearchUseCase
) : BaseViewModel() {

    sealed class ScreenState {
        data class Content(val data: List<Movie>) : ScreenState()
        object Loading : ScreenState()

        object Empty : ScreenState()

        companion object {
            fun initial(): ScreenState = Content(listOf())
        }
    }

    val viewState: MutableLiveData<ScreenState> = MutableLiveData(ScreenState.initial())
    private var state: ScreenState by viewState.delegate()

    fun onSearchMovieInputChanged(movieTitle: CharSequence) {
        if (state == ScreenState.Loading) {
            compositeDisposable.dispose() // отменяем все текущие запросы
        }
        searchUseCase.searchMovie(movieTitle)
            .doOnSubscribe { state = ScreenState.Loading }
            .flattenAsObservable { it }
            .flatMap { movie: Movie ->
                searchUseCase.fetchMovieDetails(movie.id)
                    .map { movie.copy(runtime = it.runtime ?: 0) }
                    .onErrorReturnItem(movie) // при возникновении ошибки пропускаем и идем альше
            }
            .toList()
            .scheduleIoToUi(schedulersProvider)
            .subscribe(
                { movies ->
                    state = if (movies.isEmpty()) ScreenState.Empty else ScreenState.Content(movies)
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

