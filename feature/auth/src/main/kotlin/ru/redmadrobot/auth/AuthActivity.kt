package ru.redmadrobot.auth

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_auth.*
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.auth.viewmodel.AuthViewState
import ru.redmadrobot.common.base.BaseActivity
import ru.redmadrobot.common.extensions.isVisible
import ru.redmadrobot.common.extensions.showLoading
import javax.inject.Inject

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
        AuthComponent.init(appComponent)
            .inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
        viewModel.viewState
            .observe(this, Observer { t: AuthViewState ->
                renderLoading(t.isFetching)
                renderAuthorized(t.isAuthorized)
                renderButtonChanged(t.isButtonEnabled)
                renderError(t.errorMessage)
            })
    }

    private fun initViews() {
        btn_submit.setOnClickListener {
            val login = et_login.fieldValue()
            val password = et_password.fieldValue()
            viewModel.onAuthorizeButtonClick(login, password)
        }

        et_login.doOnTextChanged { text, _, _, _ ->
            text?.let {
                viewModel.checkValuesAreValid(it.toString(), et_password.fieldValue())
            }
        }

        et_password.doOnTextChanged { text, _, _, _ ->
            text?.let {
                viewModel.checkValuesAreValid(et_login.fieldValue(), it.toString())
            }
        }
    }

    private fun renderLoading(loading: Boolean) = auth_progress_bar.showLoading(loading)

    private fun renderError(errorMessage: String?) {
        tv_error.text = errorMessage
        tv_error.isVisible(errorMessage != null)
    }

    private fun renderButtonChanged(isEnabled: Boolean) {
        btn_submit.isEnabled = isEnabled
    }

    private fun renderAuthorized(authorized: Boolean) {
        //TODO: goto next screen
    }

    private fun TextInputEditText.fieldValue(): String = text.toString()
}
