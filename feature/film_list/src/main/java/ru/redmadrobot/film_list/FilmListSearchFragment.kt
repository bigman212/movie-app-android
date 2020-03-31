package ru.redmadrobot.film_list

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.film_list.databinding.FragmentFilmSearchListBinding
import ru.redmadrobot.film_list.di.component.FilmListComponent
import javax.inject.Inject

class FilmListSearchFragment : BaseFragment(R.layout.fragment_film_search_list) {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FilmListViewModel

    private val binding: FragmentFilmSearchListBinding by viewBinding()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
//        initViewModel()
//        initViews()
    }

    private fun initDagger() {
        FilmListComponent.init(appComponent).inject(this)
    }

    companion object {
        fun newInstance() = FilmListSearchFragment()
    }
}
