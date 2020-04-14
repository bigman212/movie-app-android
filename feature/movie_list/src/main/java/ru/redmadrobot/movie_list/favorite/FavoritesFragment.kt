package ru.redmadrobot.movie_list.favorite

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.redmadrobot.common.adapters.MovieGridItem
import ru.redmadrobot.common.adapters.MovieListItem
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.movie_list.R
import ru.redmadrobot.movie_list.databinding.FragmentFavoritesBinding
import ru.redmadrobot.movie_list.di.component.MovieListComponent
import javax.inject.Inject

class FavoritesFragment : BaseFragment(R.layout.fragment_favorites) {
    companion object {
        fun newInstance() = FavoritesFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FavoritesViewModel by viewModels { viewModelFactory }

    private val binding: FragmentFavoritesBinding by viewBinding()

    private val adapterGrid = GroupAdapter<GroupieViewHolder>().apply { spanCount = 2 }
    private val adapterList = GroupAdapter<GroupieViewHolder>()
    private var contentStyleGrid = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initMovieList()
        initViewModel()
        initViews()

        viewModel.fetchFavorites()
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

    private fun renderState(state: FavoritesViewModel.ScreenState) {
        renderFetching(state is FavoritesViewModel.ScreenState.Loading)
        renderEmpty(state is FavoritesViewModel.ScreenState.Empty)
        if (state is FavoritesViewModel.ScreenState.Content) {
            renderContent(state.data)
        }
    }

    private fun renderFetching(isFetching: Boolean) {
        binding.progressBar.isVisible = isFetching
    }

    private fun renderEmpty(isEmpty: Boolean) {
        binding.rvMoviesList.isGone = isEmpty
        binding.viewStubNoFavoritesFound.root.isVisible = isEmpty
    }

    private fun renderContent(moviesFound: List<Movie>) {
        val onMovieClicked = { clickedItem: Movie ->
            val directions = FavoritesFragmentDirections.toMovieDetailFragment(clickedItem.id)
            navigateTo(directions)
        }
        val movieAdapterListItems = moviesFound.map { movie -> MovieListItem(movie, onMovieClicked) }
        val movieAdapterGridItems = moviesFound.map { movie -> MovieGridItem(movie, onMovieClicked) }
        adapterList.update(movieAdapterListItems)
        adapterGrid.update(movieAdapterGridItems)
    }

    private fun initViews() {
        val menu = binding.toolbarFavorites.menu
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.favorites_search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = TODO()
            override fun onQueryTextChange(newText: String): Boolean = TODO()
        })

        val changeContentStyleItem: MenuItem = menu.findItem(R.id.action_change_content_style)

        changeContentStyleItem.setOnMenuItemClickListener {
            if (contentStyleGrid) {
                changeContentToList()
            } else {
                changeContentToGrid()
            }
            contentStyleGrid = !contentStyleGrid
            true
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
}
