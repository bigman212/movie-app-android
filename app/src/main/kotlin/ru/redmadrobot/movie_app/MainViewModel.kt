package ru.redmadrobot.movie_app

import ru.redmadrobot.auth.AuthFragmentDirections
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.vm.NavigateToEvent
import ru.redmadrobot.core.network.SessionIdRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: SessionIdRepository) : BaseViewModel() {
    private val authLocation = AuthFragmentDirections.toAuthFragmentGlobal()
    private val defaultLocation = AuthFragmentDirections.toMovieListMainFragment()

    fun requestStartLocationEvent() {
        events.offer(
            NavigateToEvent(
                if (repository.sessionIdExists()) defaultLocation else authLocation
            )
        )
    }
}
