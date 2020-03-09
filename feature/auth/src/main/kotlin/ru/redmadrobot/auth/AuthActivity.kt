package ru.redmadrobot.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_auth.*
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.auth.viewmodel.AuthViewState
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication
import ru.redmadrobot.common.extensions.gone
import ru.redmadrobot.common.extensions.invisible
import ru.redmadrobot.common.extensions.isNotEmpty
import ru.redmadrobot.common.extensions.visible
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

        btn_submit.setOnClickListener {
            viewModel.onAuthorizeButtonClick(et_login.text.toString(), et_password.text.toString())
        }

        val editTexts = listOf(et_login, et_password)
        editTexts.forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    btn_submit.isEnabled = editTexts.all(TextInputEditText::isNotEmpty)
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    private fun renderLoading(loading: Boolean) {
        with(progress_bar) {
            if (loading) visible() else gone()
        }
    }

    private fun renderWrongCredentialsError(wrongCredentials: Boolean) {
        with(tv_error) {
            if (wrongCredentials) {
                text = getString(R.string.error_wrong_credentials)
                visible()
            } else {
                invisible()
            }
        }
    }

    private fun renderUnknownError(unknownError: Throwable?) {
        with(tv_error) {
            if (unknownError != null) {
                text = getString(R.string.error_unknown)
                visible()
            } else {
                invisible()
            }
        }
    }

    private fun renderAuthorized(authorized: Boolean) {
        //TODO: goto next screen
    }
}
