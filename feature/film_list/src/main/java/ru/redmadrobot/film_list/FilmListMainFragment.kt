package ru.redmadrobot.film_list

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_film_main_list.*
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.film_list.di.component.FilmListComponent
import javax.inject.Inject

class FilmListMainFragment : BaseFragment() {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FilmListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_film_main_list, container, false) as ViewGroup
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
//        initViewModel()
//        initViews()

        et_start_search.makeReadOnly()
        et_start_search.setOnClickListener {
            findNavController().navigate(FilmListMainFragmentDirections.toFilmListSearchFragment())
        }
    }

    private fun initDagger() {
        FilmListComponent.init(appComponent).inject(this)
    }

    companion object {
        fun newInstance() = FilmListMainFragment()
    }

    private fun EditText.makeReadOnly() {
        isFocusable = false
        isFocusableInTouchMode = false
        this.inputType = InputType.TYPE_NULL
    }
}
