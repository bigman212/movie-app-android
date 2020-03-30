package ru.redmadrobot.common.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication

open class BaseFragment : Fragment {
    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    protected val appComponent: AppProvider by lazy {
        (requireActivity().applicationContext as DaggerApplication).getApplicationProvider()
    }

    companion object {
        val TAG = this::class.java.simpleName
    }
}
