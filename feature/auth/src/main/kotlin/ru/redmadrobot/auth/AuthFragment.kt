package ru.redmadrobot.auth

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.fieldValue
import ru.redmadrobot.common.extensions.hideKeyboard
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.observeEvents
import javax.inject.Inject

class AuthFragment : BaseFragment(R.layout.fragment_auth) {
    companion object {
        fun newInstance() = AuthFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AuthViewModel by viewModels { viewModelFactory }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initViewModel()
        initViews()
    }

    private fun initDagger() {
        AuthComponent.init(appComponent).inject(this)
    }

    private fun initViewModel() {
        observe(viewModel.viewState) {
            renderLoading(it.isFetching)
            renderButtonChanged(it.isButtonEnabled)
            renderErrorView(it.errorMessage)
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

    private fun renderButtonChanged(isEnabled: Boolean) {
        btn_submit.isEnabled = isEnabled
    }

    override fun onEvent(event: Event) {
        super.onEvent(event)
        if (event is AuthViewModel.AuthorizedEvent) {
            navigateTo(AuthFragmentDirections.toMovieListMainFragment())
        }
    }
}
