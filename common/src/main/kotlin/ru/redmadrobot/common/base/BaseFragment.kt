package ru.redmadrobot.common.base

import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.redmadrobot.common.R
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication
import ru.redmadrobot.common.vm.ErrorEvent
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.MessageEvent
import ru.redmadrobot.common.vm.NavigateToEvent

open class BaseFragment : Fragment {
    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    companion object {
        private val ENTER_DEFAULT_ANIMATION = R.anim.nav_default_enter_anim
        private val EXIT_DEFAULT_ANIMATION = R.anim.nav_default_exit_anim
        private val POP_ENTER_DEFAULT_ANIMATION = R.anim.nav_default_pop_enter_anim
        private val POP_EXIT_DEFAULT_ANIMATION = R.anim.nav_default_pop_exit_anim

        private const val DEFAULT_ANIMATION = -1
    }

    protected val appComponent: AppProvider by lazy {
        (requireActivity().applicationContext as DaggerApplication).getApplicationProvider()
    }

    @CallSuper
    protected open fun onEvent(event: Event) {
        when (event) {
            is MessageEvent -> showMessage(event.message)
            is ErrorEvent -> showError(event.errorMessage)
            is NavigateToEvent -> {
                findNavController().navigate(event.direction)
            }
        }
    }

    private fun showMessage(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showError(errorMessage: CharSequence) {
        val contentView = requireActivity().findViewById<View>(android.R.id.content)
        Snackbar.make(contentView, errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    protected fun navigateTo(direction: NavDirections, navOptions: NavOptions? = null) {
        val finalNavOptions = createNavOptions(direction, navOptions)
        findNavController().navigate(direction, finalNavOptions)
    }

    private fun createNavOptions(direction: NavDirections, navOptions: NavOptions?): NavOptions {
        val navOptionsFromNavAction = findNavController().currentDestination
            ?.getAction(direction.actionId)
            ?.navOptions

        val currentNavOptions = when {
            navOptions != null -> navOptions
            navOptionsFromNavAction != null -> navOptionsFromNavAction
            else -> NavOptions.Builder().build()
        }

        with(currentNavOptions) {
            val enterAnimation = enterAnim.takeIf { it != DEFAULT_ANIMATION } ?: ENTER_DEFAULT_ANIMATION
            val exitAnimation = exitAnim.takeIf { it != DEFAULT_ANIMATION } ?: EXIT_DEFAULT_ANIMATION
            val popEnterAnimation =
                popEnterAnim.takeIf { it != DEFAULT_ANIMATION } ?: POP_ENTER_DEFAULT_ANIMATION
            val popExitAnimation = popExitAnim.takeIf { it != DEFAULT_ANIMATION } ?: POP_EXIT_DEFAULT_ANIMATION

            return NavOptions.Builder()
                .setEnterAnim(enterAnimation)
                .setExitAnim(exitAnimation)
                .setPopEnterAnim(popEnterAnimation)
                .setPopExitAnim(popExitAnimation)
                .setPopUpTo(popUpTo, isPopUpToInclusive)
                .setLaunchSingleTop(shouldLaunchSingleTop())
                .build()
        }
    }
}
