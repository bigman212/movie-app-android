package ru.redmadrobot.movie_list.search

import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.Disposable
import ru.redmadrobot.common.adapters.Movie
import ru.redmadrobot.common.adapters.MovieListItem
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.extensions.showKeyboard
import ru.redmadrobot.common.extensions.showLoading
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.movie_list.R
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

    private val viewModel: MovieListSearchViewModel by viewModels { viewModelFactory }

    private val binding: FragmentMovieSearchListBinding by viewBinding()

    private val adapter: GroupAdapter<GroupieViewHolder> = GroupAdapter()

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
        binding.rvMoviesList.layoutManager = LinearLayoutManager(this.context)
        binding.rvMoviesList.adapter = adapter
    }

    private fun initViewModel() {
        observe(viewModel.viewState, ::renderState)
        observeEvents(viewModel.events, ::onEvent)
    }

    private fun renderState(state: MovieListSearchViewModel.ScreenState) {
        renderFetching(state is MovieListSearchViewModel.ScreenState.Loading)
        renderEmpty(state is MovieListSearchViewModel.ScreenState.Empty)
        if (state is MovieListSearchViewModel.ScreenState.Content) {
            renderContent(state.data)
        }
    }

    private fun renderFetching(isFetching: Boolean) = binding.progressBar.showLoading(isFetching)

    private fun renderEmpty(isEmpty: Boolean) {
        binding.rvMoviesList.isGone = isEmpty
        binding.groupNoMoviesFound.isVisible = isEmpty
    }

    private fun renderContent(moviesFound: List<Movie>) {
        val movieAdapterItems = moviesFound.map(::MovieListItem)
        adapter.update(movieAdapterItems)
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

    override fun onDestroy() {
        super.onDestroy()
        searchTextObserver.dispose()
    }
}
