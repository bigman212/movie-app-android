package ru.redmadrobot.common.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication

open class BaseActivity : AppCompatActivity {
    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    companion object {
        val TAG = this::class.java.simpleName
    }

    protected val appComponent: AppProvider by lazy {
        (applicationContext as DaggerApplication).getApplicationProvider()
    }
}
