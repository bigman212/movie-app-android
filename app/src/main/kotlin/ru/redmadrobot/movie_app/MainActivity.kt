package ru.redmadrobot.movie_app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import ru.redmadrobot.common.base.BaseActivity
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.NavigateToEvent
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.movie_app.di.AppComponent
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (appComponent as AppComponent).inject(this)

        val navController = findNavController(R.id.root_nav_host_fragment)
        menu_navigation.setupWithNavController(navController)
        setupBottomNavigationBarVisibility(navController)

        observeEvents(viewModel.events, ::onEvent)
        viewModel.requestStartLocationEvent()
    }

    private fun onEvent(event: Event) {
        if (event is NavigateToEvent) {
            findNavController(R.id.root_nav_host_fragment).navigate(event.direction)
        } else {
            Timber.e(IllegalArgumentException("Unknown Event Type"))
        }
    }

    private fun setupBottomNavigationBarVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_auth_fragment -> menu_navigation.isVisible = false
                else -> menu_navigation.isVisible = true
            }
        }
    }
}
