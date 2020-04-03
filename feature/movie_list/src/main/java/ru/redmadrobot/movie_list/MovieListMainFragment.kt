package ru.redmadrobot.movie_list

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.movie_list.databinding.FragmentMovieMainListBinding
import ru.redmadrobot.movie_list.di.component.MovieListComponent

class MovieListMainFragment : BaseFragment(R.layout.fragment_movie_main_list) {
    companion object {
        fun newInstance() = MovieListMainFragment()
    }

    private val binding: FragmentMovieMainListBinding by viewBinding()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDagger()
        initViews()
    }

    private fun initDagger() {
        MovieListComponent.init(appComponent).inject(this)
    }

    private fun initViews() {
        binding.etStartSearch.makeReadOnly()
        binding.etStartSearch.setOnClickListener {
            navigateTo(MovieListMainFragmentDirections.toMovieListSearchFragment())
        }
    }

    private fun EditText.makeReadOnly() {
        isFocusable = false
        isFocusableInTouchMode = false
        this.inputType = InputType.TYPE_NULL
    }
}
