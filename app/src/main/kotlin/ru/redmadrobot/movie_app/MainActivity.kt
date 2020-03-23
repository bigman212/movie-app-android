package ru.redmadrobot.movie_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.redmadrobot.core.network.SessionIdRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var sessionRepo: SessionIdRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sessionRepo.sessionIdExists()) {
            //todo: goto screen 2
        } else {
            //todo: goto auth screen
        }
    }
}
