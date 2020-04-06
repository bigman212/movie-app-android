package ru.redmadrobot.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.redmadrobot.auth.AuthFragmentDirections
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.profile.databinding.FragmentProfileBinding
import ru.redmadrobot.profile.di.component.ProfileComponent
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    private val binding: FragmentProfileBinding by viewBinding()

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initViewModel()


        binding.btnLogout.setOnClickListener {
            viewModel.onLogoutButtonClicked()
        }
    }

    private fun initDagger() {
        ProfileComponent.init(appComponent).inject(this)
    }

    private fun initViewModel() {
        observe(viewModel.viewState) {
            renderFetching(it.isFetching)
            renderLogoutButtonState(it.isLogoutButtonEnabled)
        }

        observeEvents(viewModel.events, ::onEvent)
    }

    private fun renderFetching(isFetching: Boolean) {
        binding.progressBar.showLoading(isFetching)
    }

    private fun renderLogoutButtonState(buttonState: Boolean) {
        binding.btnLogout.isEnabled = buttonState
    }

    override fun onEvent(event: Event) {
        super.onEvent(event)
        if (event is ProfileViewModel.LogoutEvent) {
            navigateTo(AuthFragmentDirections.toAuthFragmentGlobal())
        }
    }
}
