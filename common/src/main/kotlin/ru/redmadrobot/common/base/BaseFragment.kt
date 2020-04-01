package ru.redmadrobot.common.base

import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.vm.ErrorEvent
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.MessageEvent

open class BaseFragment : Fragment {
    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

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
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_SHORT)
            .show()
    }

    inline fun <T, LD : LiveData<T>> Fragment.observe(liveData: LD, crossinline block: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner) {
            block.invoke(it)
        }
    }

    companion object {
        val TAG = this::class.java.simpleName
    }
}
