package ru.redmadrobot.movie_list.search

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.core.network.SchedulersProvider
import ru.redmadrobot.core.network.scheduleIoToUi
import ru.redmadrobot.movie_list.R
import ru.redmadrobot.movie_list.adapters.MoviesListAdapter
import ru.redmadrobot.movie_list.data.MovieSearchService
import ru.redmadrobot.movie_list.databinding.FragmentMovieSearchListBinding
import ru.redmadrobot.movie_list.di.component.MovieListComponent
import timber.log.Timber
import javax.inject.Inject

class MovieListSearchFragment : BaseFragment(R.layout.fragment_movie_search_list) {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var api: MovieSearchService

    private lateinit var viewModel: MovieListViewModel

    private val binding: FragmentMovieSearchListBinding by viewBinding()

    private lateinit var adapter: MoviesListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initMovieList()
//        initViewModel()
//        initViews()
    }

    private fun initDagger() {
        MovieListComponent.init(appComponent).inject(this)
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
        fun newInstance() = MovieListSearchFragment()
    }
}
