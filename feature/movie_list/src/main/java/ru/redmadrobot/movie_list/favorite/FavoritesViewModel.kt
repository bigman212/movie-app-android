package ru.redmadrobot.movie_list.favorite

import androidx.lifecycle.MutableLiveData
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.extensions.delegate
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.movie_list.favorite.domain.FavoritesUseCase
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val favoritesUseCase: FavoritesUseCase
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

    fun fetchFavorites() {
        favoritesUseCase.getFavoriteMovies()
            .doOnSubscribe { state = ScreenState.Loading }
            .scheduleIoToUi(schedulersProvider)
            .subscribe(
                this::handleFavoritesMovies,
                this::offerErrorEvent
            ).disposeOnCleared()
    }

    private fun handleFavoritesMovies(favoriteMovies: List<Movie>) {
        state = if (favoriteMovies.isEmpty()) {
            ScreenState.Empty
        } else {
            ScreenState.Content(favoriteMovies)
        }
    }
}
