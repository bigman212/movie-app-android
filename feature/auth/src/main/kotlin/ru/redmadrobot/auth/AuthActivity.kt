package ru.redmadrobot.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.auth.viewmodel.AuthViewState
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication
import javax.inject.Inject

class AuthActivity : AppCompatActivity(R.layout.activity_auth) {

    val appComponent: AppProvider by lazy {
        (applicationContext as DaggerApplication).getApplicationProvider()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthComponent.Builder
            .build(appComponent)
            .inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
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
