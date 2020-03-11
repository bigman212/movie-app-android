package ru.redmadrobot.auth

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_auth.*
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.auth.viewmodel.AuthViewState
import ru.redmadrobot.common.base.BaseActivity
import ru.redmadrobot.common.extensions.isVisible
import ru.redmadrobot.common.extensions.showLoading
import javax.inject.Inject

//todo(help): попробовать поднимать кнопку при поднятии клавиатуры
class AuthActivity : BaseActivity(R.layout.activity_auth) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDagger()
        initViewModel()
        initViews()
    }

    private fun initDagger() {
        AuthComponent.Builder
            .build(appComponent)
            .inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
        viewModel.viewState
            .observe(this, Observer { t: AuthViewState ->
                renderLoading(t.isFetching)
                renderAuthorized(t.isAuthorized)
                renderButtonChanged(t.isButtonEnabled)
                renderError(t.error)
            })
    }

    private fun initViews() {
        btn_submit.setOnClickListener {
            viewModel.onAuthorizeButtonClick()
        }

        et_login.doOnTextChanged { text, _, _, _ ->
            text?.let {
                viewModel.loginFieldValue = it.toString()
            }
        }

        et_password.doOnTextChanged { text, _, _, _ ->
            text?.let {
                viewModel.passwordFieldValue = it.toString()
            }
        }
    }

    private fun renderLoading(loading: Boolean) = progress_bar.showLoading(loading)

    private fun renderError(error: Throwable?) {
        tv_error.text = error?.message ?: ""
        tv_error.isVisible(error != null)
    }

    private fun renderButtonChanged(isEnabled: Boolean) {
        btn_submit.isEnabled = isEnabled
    }


    private fun renderAuthorized(authorized: Boolean) {
        //TODO: goto next screen
    }
}
