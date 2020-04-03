package ru.redmadrobot.movie_app

import androidx.navigation.NavDirections
import ru.redmadrobot.auth.AuthFragmentDirections
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.core.network.SessionIdRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: SessionIdRepository) : BaseViewModel() {

    fun requestStartLocationEvent() {
        events.offer(
            NavigateToStartLocation(
                if (repository.sessionIdExists()) defaultLocation else authLocation
            )
        )
    }

    internal class NavigateToStartLocation(val destination: NavDirections) : Event

    private val authLocation = AuthFragmentDirections.toAuthFragmentGlobal()
    private val defaultLocation = AuthFragmentDirections.toMovieListMainFragment()
}
