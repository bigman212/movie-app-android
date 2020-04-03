package ru.redmadrobot.common.base

import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.redmadrobot.common.R
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.vm.ErrorEvent
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.MessageEvent

open class BaseFragment : Fragment {
    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    companion object {
        private val ENTER_DEFAULT_ANIMATION = R.anim.nav_default_enter_anim
        private val EXIT_DEFAULT_ANIMATION = R.anim.nav_default_exit_anim
        private val POP_ENTER_DEFAULT_ANIMATION = R.anim.nav_default_pop_enter_anim
        private val POP_EXIT_DEFAULT_ANIMATION = R.anim.nav_default_pop_exit_anim
    }

    protected val appComponent: AppProvider by lazy {
        (requireActivity().applicationContext as DaggerApplication).getApplicationProvider()
    }

    @CallSuper
    protected open fun onEvent(event: Event) {
        when (event) {
            is MessageEvent -> showMessage(event.message)
            is ErrorEvent -> showError(event.errorMessage)
        }
    }

    private fun showMessage(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showError(errorMessage: CharSequence) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    inline fun <T, LD : LiveData<T>> observe(liveData: LD, crossinline block: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner) {
            block.invoke(it)
        }
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
            val enterAnimation = enterAnim.takeIf { it != ENTER_DEFAULT_ANIMATION } ?: ENTER_DEFAULT_ANIMATION
            val exitAnimation = exitAnim.takeIf { it != EXIT_DEFAULT_ANIMATION } ?: EXIT_DEFAULT_ANIMATION
            val popEnterAnimation =
                popEnterAnim.takeIf { it != POP_ENTER_DEFAULT_ANIMATION } ?: POP_ENTER_DEFAULT_ANIMATION
            val popExitAnimation = popExitAnim.takeIf { it != POP_EXIT_DEFAULT_ANIMATION } ?: POP_EXIT_DEFAULT_ANIMATION

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
