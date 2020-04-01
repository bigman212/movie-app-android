package ru.redmadrobot.auth

import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.redmadrobot.auth.di.component.AuthComponent
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.auth.viewmodel.AuthViewState
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.hideKeyboard
import ru.redmadrobot.common.extensions.isVisible
import ru.redmadrobot.common.extensions.showLoading
import javax.inject.Inject

class AuthFragment : BaseFragment(R.layout.fragment_auth) {
    companion object {
        fun newInstance() = AuthFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AuthViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initViewModel()
        initViews()
    }

    private fun initDagger() {
        AuthComponent.init(appComponent).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val themedInflator = inflater.cloneInContext(wrapContextTheme(activity, R.style.AuthFragmentTheme))
        requireActivity().theme?.applyStyle(R.style.AuthFragmentTheme, true)
        return inflater.inflate(R.layout.fragment_auth, container, false) as ViewGroup
    }

//    fun wrapContextTheme(activity: Activity?, @StyleRes styleRes: Int): Context? {
//        val contextThemeWrapper = ContextThemeWrapper(activity, styleRes)
//
//        //Sets the navigation bar and statusbar color
//        (fetchPrimaryDarkColor(contextThemeWrapper.theme), activity)
//        return contextThemeWrapper
//    }

    fun fetchPrimaryDarkColor(context: Resources.Theme): Int {
        val typedValue = TypedValue()
        val a: TypedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimaryDark))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
        viewModel.viewState.observe(viewLifecycleOwner, Observer { t: AuthViewState ->
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

    private fun renderError(errorMessage: String?) {
        tv_error.text = errorMessage
        tv_error.isVisible(errorMessage != null)
    }

    private fun renderButtonChanged(isEnabled: Boolean) {
        btn_submit.isEnabled = isEnabled
    }

    private fun renderAuthorized(authorized: Boolean) {
        if (authorized) {
            val toFilmListFragment = AuthFragmentDirections.toMovieListMainFragment()
            findNavController().navigate(toFilmListFragment)
        }
    }

    private fun TextInputEditText.fieldValue(): String = text.toString()

}
