package ru.redmadrobot.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.data.profile.AccountDetails
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.profile.databinding.FragmentProfileBinding
import ru.redmadrobot.profile.di.component.ProfileComponent
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
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

        viewModel.loadAccountDetails()
    }

    private fun initDagger() {
        ProfileComponent.init(appComponent).inject(this)
    }

    private fun initViewModel() {
        observe(viewModel.viewState) { state ->
            renderFetching(state.isFetching)
            renderLogoutButtonState(state.isLogoutButtonEnabled)
            renderAccount(state.account)
        }

        observeEvents(viewModel.events, ::onEvent)
    }

    private fun renderAccount(account: AccountDetails?) {
        account?.let { accountDetails ->
            binding.tvFullName.text = accountDetails.username
        }
    }

    private fun renderFetching(isFetching: Boolean) {
        binding.progressBar.showLoading(isFetching)
    }

    private fun renderLogoutButtonState(buttonState: Boolean) {
        binding.btnLogout.isEnabled = buttonState
    }
}
