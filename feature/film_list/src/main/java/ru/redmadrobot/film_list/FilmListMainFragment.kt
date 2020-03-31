package ru.redmadrobot.film_list

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.film_list.databinding.FragmentFilmMainListBinding
import ru.redmadrobot.film_list.di.component.FilmListComponent

class FilmListMainFragment : BaseFragment(R.layout.fragment_film_main_list) {
    private val binding: FragmentFilmMainListBinding by viewBinding()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        initDagger()
//        initViewModel()
//        initViews()

        binding.etStartSearch.makeReadOnly()

        binding.etStartSearch.setOnClickListener {
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
