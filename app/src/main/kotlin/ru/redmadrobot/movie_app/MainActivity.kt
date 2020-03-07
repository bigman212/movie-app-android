package ru.redmadrobot.movie_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.redmadrobot.core.network.MoviesService
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    internal lateinit var apiClient: MoviesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
    }
}
