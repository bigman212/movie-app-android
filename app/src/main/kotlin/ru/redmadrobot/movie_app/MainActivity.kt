package ru.redmadrobot.movie_app

import android.os.Bundle
import ru.redmadrobot.common.base.BaseActivity
import ru.redmadrobot.core.network.SessionIdRepository
import ru.redmadrobot.movie_app.di.AppComponent
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    lateinit var sessionRepo: SessionIdRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (appComponent as AppComponent).inject(this)
    }
}
