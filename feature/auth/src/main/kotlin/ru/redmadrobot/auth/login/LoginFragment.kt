package ru.redmadrobot.auth.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_login.*
import ru.redmadrobot.auth.R
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.auth.login.viewmodel.LoginViewModel
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.fieldValue
import ru.redmadrobot.common.extensions.hideKeyboard
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.observeEvents
import javax.inject.Inject

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDagger()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
    }

    private fun initDagger() {
        AuthComponent.init(appComponent).inject(this)
    }

    private fun initViewModel() {
        observe(viewModel.viewState) { state ->
            renderLoading(state.isFetching)
            renderButtonChanged(state.isButtonEnabled)
            renderErrorView(state.errorMessage)
        }
        observeEvents(viewModel.events, ::onEvent)
    }

    private fun initViews() {
        btn_submit.setOnClickListener {
            val login = et_login.fieldValue()
            val password = et_password.fieldValue()
            viewModel.onAuthorizeButtonClick(login, password)

            hideKeyboard(requireActivity().currentFocus)
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

    private fun renderErrorView(errorMessage: String?) {
        tv_error.text = errorMessage
        tv_error.isVisible = errorMessage != null
    }

    override fun onResume() {
        super.onResume()
        et_password.setText("")
    }

    private fun renderButtonChanged(isEnabled: Boolean) {
        btn_submit.isEnabled = isEnabled
    }

    override fun onEvent(event: Event) {
        super.onEvent(event)
        if (event is LoginViewModel.AuthorizedEvent) {
            navigateTo(LoginFragmentDirections.toPinCreateFragment())
        }
    }
}
