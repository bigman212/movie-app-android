package ru.redmadrobot.movie_app

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import ru.redmadrobot.auth.AuthFragmentDirections
import ru.redmadrobot.common.base.BaseActivity
import ru.redmadrobot.common.extensions.gone
import ru.redmadrobot.common.extensions.visible
import ru.redmadrobot.core.network.SessionIdRepository
import ru.redmadrobot.movie_app.di.AppComponent
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    lateinit var sessionRepo: SessionIdRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (appComponent as AppComponent).inject(this)

        val findNavController = findNavController(R.id.root_nav_host_fragment)
        bottomNavigationBarVisibility(findNavController)

        if (sessionRepo.sessionIdExists()) {
            findNavController.navigate(AuthFragmentDirections.toMovieListMainFragment())
        }
    }

    private fun bottomNavigationBarVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_auth_fragment -> menu_navigation.gone()
                else -> menu_navigation.visible()
            }
        }
    }
}
