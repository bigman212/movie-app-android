package ru.redmadrobot.movie_app

import ru.redmadrobot.auth.login.LoginFragmentDirections
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.vm.NavigateToEvent
import ru.redmadrobot.core.network.SessionIdRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: SessionIdRepository) : BaseViewModel() {
    private val authLocation = LoginFragmentDirections.toLoginFragmentGlobal()
    private val defaultLocation = LoginFragmentDirections.toMovieListMainFragment()

    fun requestStartLocationEvent() {
        events.offer(
            NavigateToEvent(
                if (repository.sessionIdExists()) defaultLocation else authLocation
            )
        )
    }
}
