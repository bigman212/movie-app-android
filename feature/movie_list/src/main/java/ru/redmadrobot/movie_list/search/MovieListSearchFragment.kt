package ru.redmadrobot.movie_list.search

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.Disposable
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.showKeyboard
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.movie_list.Movie
import ru.redmadrobot.movie_list.R
import ru.redmadrobot.movie_list.adapters.MoviesListAdapter
import ru.redmadrobot.movie_list.databinding.FragmentMovieSearchListBinding
import ru.redmadrobot.movie_list.di.component.MovieListComponent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieListSearchFragment : BaseFragment(R.layout.fragment_movie_search_list) {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MovieListSearchViewModel

    private val binding: FragmentMovieSearchListBinding by viewBinding()

    private lateinit var adapter: MoviesListAdapter

    private lateinit var searchTextObserver: Disposable

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initMovieList()
        initViewModel()

        binding.etSearchInput.requestFocus()
        showKeyboard()

        searchTextObserver = binding.etSearchInput.textChanges()
            .skipInitialValue()
            .map(CharSequence::trim)
            .debounce(1, TimeUnit.SECONDS)
            .subscribe(viewModel::onSearchMovieInputChanged)
    }

    private fun initDagger() {
        MovieListComponent.init(appComponent).inject(this)
    }

    private fun initMovieList() {
        adapter = MoviesListAdapter()
        binding.rvMoviesList.layoutManager = LinearLayoutManager(this.context)
        binding.rvMoviesList.adapter = adapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieListSearchViewModel::class.java]

        observe(viewModel.isFetching, ::renderFetching)
        observe(viewModel.movies, ::renderMovies)
        observeEvents(viewModel.events, ::onEvent)
    }

    private fun renderFetching(isFetching: Boolean) {
        binding.progressBar.showLoading(isFetching)
    }

    private fun renderMovies(movies: List<Movie>) {
        adapter.addAll(movies)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchTextObserver.dispose()
    }

    companion object {
        fun newInstance() = MovieListSearchFragment()
    }
}
