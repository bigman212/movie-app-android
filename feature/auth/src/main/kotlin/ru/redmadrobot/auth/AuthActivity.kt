package ru.redmadrobot.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.auth.viewmodel.AuthViewState
import javax.inject.Inject

class AuthActivity : AppCompatActivity(R.layout.activity_auth) {

    @Inject
    internal lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewState()
            .observe(this, Observer { t: AuthViewState ->
                renderLoading(t.isLoading)
                renderWrongCredentialsError(t.wrongCredentials)
                renderUnknownError(t.unknownError)
                renderAuthorized(t.isAuthorized)
            })
    }

    private fun renderLoading(loading: Boolean) {

    }

    private fun renderWrongCredentialsError(wrongCredentials: Boolean) {

    }

    private fun renderUnknownError(unknownError: Throwable?) {

    }

    private fun renderAuthorized(authorized: Boolean) {

    }
}
