package ru.redmadrobot.movie_list.favorite

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.movie_list.favorite.domain.FavoritesUseCase
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val searchUseCase: FavoritesUseCase
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
        if (state == ScreenState.Loading && !compositeDisposable.isDisposed) {
            compositeDisposable.clear() // отменяем все текущие запросы используя именно clear, а не dispose
        }
    }
}
