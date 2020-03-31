package ru.redmadrobot.film_list

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.film_list.adapters.MoviesListAdapter
import ru.redmadrobot.film_list.databinding.FragmentFilmSearchListBinding
import ru.redmadrobot.film_list.di.component.FilmListComponent
import timber.log.Timber
import javax.inject.Inject

class FilmListSearchFragment : BaseFragment(R.layout.fragment_film_search_list) {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var api: MovieSearchService

    private lateinit var viewModel: FilmListViewModel

    private val binding: FragmentFilmSearchListBinding by viewBinding()

    private lateinit var adapter: MoviesListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initMovieList()
//        initViewModel()
//        initViews()
    }

    private fun initDagger() {
        FilmListComponent.init(appComponent).inject(this)
    }

    private fun initMovieList() {
        adapter = MoviesListAdapter()
        binding.rvMoviesList.layoutManager = LinearLayoutManager(this.context)
        binding.rvMoviesList.adapter = adapter
        api.popularMovies()
            .scheduleIoToUi(SchedulersProvider())
            .subscribe(
                {
                    val results = it.results
                    adapter.addAll(results)
                }, {
                    Timber.e(it)
                })
    }

    companion object {
        fun newInstance() = FilmListSearchFragment()
    }
}
