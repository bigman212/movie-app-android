package ru.redmadrobot.auth

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.fieldValue
import ru.redmadrobot.common.extensions.hideKeyboard
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.observeEvents
import javax.inject.Inject
import javax.inject.Provider

class AuthFragment @Inject constructor(private val provider: Provider<AuthViewModel>) :
    BaseFragment(R.layout.fragment_auth) {

    //    @Inject
//    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AuthViewModel by viewModelWithProvider { provider.get() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initViewModel()
        initViews()
    }

    private inline fun <reified T : ViewModel> Fragment.viewModelWithProvider(
        noinline ownerProducer: () -> ViewModelStoreOwner = { this },
        crossinline provider: () -> T
    ) = viewModels<T>(ownerProducer) {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return provider.invoke() as T
            }
        }
    }

    private fun initDagger() {
        AuthComponent.init(appComponent).inject(this)
    }

    private fun initViewModel() {
//        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

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
