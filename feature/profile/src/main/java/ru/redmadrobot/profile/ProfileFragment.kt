package ru.redmadrobot.profile

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.extensions.viewBinding
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

    private lateinit var viewModel: ProfileViewModel

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
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        viewModel.liveState.observe(viewLifecycleOwner, Observer {
            renderFetching(it.isFetching)
            renderButtonState(it.isButtonEnabled)
            renderLogout(it.isLogout)

            renderError(it.errorMessage)
        })
    }

    private fun renderError(error: String?) {
        error?.let {
            Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderFetching(isFetching: Boolean) {
        binding.progressBar.showLoading(isFetching)
    }

    private fun renderButtonState(buttonState: Boolean) {
        binding.btnLogout.isEnabled = buttonState
    }

    private fun renderLogout(isLogout: Boolean) {
        if (isLogout) {
            findNavController().navigate(ProfileFragmentDirections.toAuthFragment())
        }
    }
}
