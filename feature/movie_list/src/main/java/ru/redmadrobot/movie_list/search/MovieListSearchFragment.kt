package ru.redmadrobot.movie_list.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_movie_search_list.*
import ru.redmadrobot.common.adapters.MovieGridItem
import ru.redmadrobot.common.adapters.MovieListItem
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.extensions.showKeyboard
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.movie_list.R
import ru.redmadrobot.movie_list.databinding.FragmentMovieSearchListBinding
import ru.redmadrobot.movie_list.di.component.MovieListComponent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieListSearchFragment : BaseFragment(R.layout.fragment_movie_search_list) {
    companion object {
        private const val USER_INPUT_DEBOUNCE = 500L
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieListSearchViewModel by viewModels { viewModelFactory }

    private val binding: FragmentMovieSearchListBinding by viewBinding()

    private val adapterGrid = GroupAdapter<GroupieViewHolder>().apply { spanCount = 2 }
    private val adapterList = GroupAdapter<GroupieViewHolder>()

    private lateinit var searchTextObserver: Disposable

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDagger()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMovieList()
        initViewModel()
        initViews()
    }

    private fun initDagger() {
        MovieListComponent.init(appComponent).inject(this)
    }

    private fun initMovieList() {
        binding.rvMoviesList.layoutManager = LinearLayoutManager(context)
        binding.rvMoviesList.adapter = adapterList
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

    private fun renderFetching(isFetching: Boolean) {
        binding.progressBar.isVisible = isFetching
    }

    private fun renderEmpty(isEmpty: Boolean) {
        binding.rvMoviesList.isGone = isEmpty
        binding.viewSearchStub.isVisible = isEmpty
    }

    private fun renderContent(moviesFound: List<Movie>) {
        val onMovieClicked = { clickedItem: Movie ->
            val directions = MovieListSearchFragmentDirections.toMovieDetailFragment(clickedItem.id)
            navigateTo(directions)
        }
        val movieAdapterListItems = moviesFound.map { movie -> MovieListItem(movie, onMovieClicked) }
        val movieAdapterGridItems = moviesFound.map { movie -> MovieGridItem(movie, onMovieClicked) }
        adapterList.update(movieAdapterListItems)
        adapterGrid.update(movieAdapterGridItems)
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

        btn_change_content_style.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                changeContentToGrid()
            } else {
                changeContentToList()
            }
        }
    }

    private fun changeContentToGrid() {
        binding.rvMoviesList.layoutManager = GridLayoutManager(requireContext(), adapterGrid.spanCount)
        binding.rvMoviesList.adapter = adapterGrid
    }

    private fun changeContentToList() {
        binding.rvMoviesList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMoviesList.adapter = adapterList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchTextObserver.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
