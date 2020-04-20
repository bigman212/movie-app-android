package ru.redmadrobot.auth.pin

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.redmadrobot.auth.R
import ru.redmadrobot.auth.databinding.FragmentPincodeBinding
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.observeEvents
import javax.inject.Inject

class PinCreateFragment : BaseFragment(R.layout.fragment_pincode) {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: PinCreateViewModel by viewModels { viewModelFactory }

    private val binding: FragmentPincodeBinding by viewBinding()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDagger()
    }

    private fun initDagger() {
        AuthComponent.init(appComponent).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pincodeKeyboard.setOnInputFilledListener(viewModel::onPinFullyEntered)
        observeEvents(viewModel.events, ::onEvent)
    }

    override fun onEvent(event: Event) {
        super.onEvent(event)
        when (event) {
            is PinCreateViewModel.PinsAreNotEqualEvent -> {
                binding.tvPinTitle.text = getString(R.string.pin_repeated_wrong)
                binding.pincodeKeyboard.clearInputValues()
                binding.pincodeKeyboard.setErrorIndicators(true)
            }
            is PinCreateViewModel.ReEnterPin -> {
                binding.pincodeKeyboard.clearInputValues()
                binding.tvPinTitle.text = getString(R.string.pin_enter_pin_again)
            }
        }
    }
}
