package ru.redmadrobot.movie_app

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import ru.redmadrobot.common.base.BaseActivity
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.EventsQueue
import ru.redmadrobot.movie_app.di.AppComponent
import java.util.Queue
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (appComponent as AppComponent).inject(this)
//        оставлено для будущей реализации инъекции фрагментов через конструктор
        supportFragmentManager.fragmentFactory = appComponent.fragmentFactory()
        super.onCreate(savedInstanceState)

        val navController = findNavController(R.id.root_nav_host_fragment)
        menu_navigation.setupWithNavController(navController)
        setupBottomNavigationBarVisibility(navController)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        observeEvents(viewModel.events) {
            if (it is MainViewModel.NavigateToStartLocation) navController.navigate(it.destination)
        }
        viewModel.requestStartLocationEvent()
    }

    private fun observeEvents(eventsQueue: EventsQueue, eventHandler: (Event) -> Unit) {
        eventsQueue.observe(this) { queue: Queue<Event>? ->
            while (queue != null && queue.isNotEmpty()) {
                eventHandler(queue.remove())
            }
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
