package ru.redmadrobot.movie_list.search

import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.Disposable
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.showKeyboard
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.movie_list.R
import ru.redmadrobot.movie_list.adapters.MoviesListAdapter
import ru.redmadrobot.movie_list.data.entity.Movie
import ru.redmadrobot.movie_list.databinding.FragmentMovieSearchListBinding
import ru.redmadrobot.movie_list.di.component.MovieListComponent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieListSearchFragment : BaseFragment(R.layout.fragment_movie_search_list) {
    companion object {
        fun newInstance() = MovieListSearchFragment()

        private const val USER_INPUT_DEBOUNCE = 500L
    }

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
        initViews()
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
        observeEvents(viewModel.events, ::onEvent)
    }

    private fun renderFetching(isFetching: Boolean) {
        binding.progressBar.showLoading(isFetching)
    }

    override fun onEvent(event: Event) {
        super.onEvent(event)
        when (event) {
            is MovieListSearchViewModel.MovieSearchFinishedEvent -> showMoviesFound(event.moviesFound)
            is MovieListSearchViewModel.MovieSearchRuntimeFetchedEvent ->
                adapter.updateMovieRuntime(event.fetchedMovie.id, event.fetchedMovie.runtime ?: 0)
        }
    }

    private fun initViews() {
        binding.etSearchInput.requestFocus()
        showKeyboard()

        searchTextObserver = binding.etSearchInput.textChanges()
            .skipInitialValue()
            .map(CharSequence::trim)
            .filter(CharSequence::isNotBlank)
            .debounce(USER_INPUT_DEBOUNCE, TimeUnit.MILLISECONDS)
            .subscribe(viewModel::onSearchMovieInputChanged)
    }

    private fun showMoviesFound(moviesFound: List<Movie>) {
        adapter.replaceAllItems(moviesFound)

        val noMoviesFound = moviesFound.isEmpty()
        binding.rvMoviesList.isGone = noMoviesFound
        binding.groupNoMoviesFound.isVisible = noMoviesFound
    }

    override fun onDestroy() {
        super.onDestroy()
        searchTextObserver.dispose()
    }
}
