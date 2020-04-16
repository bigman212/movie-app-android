package ru.redmadrobot.movie_detail

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.observe
import ru.redmadrobot.common.extensions.viewBinding
import ru.redmadrobot.common.vm.observeEvents
import ru.redmadrobot.movie_detail.adapters.MovieDetailBodyItem
import ru.redmadrobot.movie_detail.adapters.MovieDetailHeaderItem
import ru.redmadrobot.movie_detail.databinding.FragmentMovieDetailBinding
import ru.redmadrobot.movie_detail.di.component.MovieDetailComponent
import javax.inject.Inject

class MovieDetailFragment : BaseFragment(R.layout.fragment_movie_detail) {
    companion object {
        private const val ARGS_PROFILE_ID = "ARGS_PROFILE_ID"
        fun newInstance(movieId: String) = MovieDetailFragment().apply {
            arguments = bundleOf(ARGS_PROFILE_ID to movieId)
        }
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailViewModel by viewModels { viewModelFactory }

    private val binding: FragmentMovieDetailBinding by viewBinding()

    private val args: MovieDetailFragmentArgs by navArgs()

    private val contentAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
        initViews()
        initViewModel()
    }

    private fun initDagger() {
        MovieDetailComponent.init(appComponent).inject(this)
    }

    private fun initViews() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        with(binding.toolbarMovieDetail) {
            inflateMenu(R.menu.menu_toolbar_movie)
            setupWithNavController(navController, appBarConfiguration)
            setNavigationIcon(R.drawable.ic_arrow_back)

            setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_mark_movie_favorite) { // вынести как ивент
                    viewModel.onFavoriteButtonClicked(args.movieId)
                    item.setIcon(R.drawable.ic_favorite_checked)
                }
                true
            }
        }

        with(binding.rvMovieContent) {
            layoutManager = LinearLayoutManager(context)
            this.adapter = contentAdapter
        }
    }

    private fun initViewModel() {
        observe(viewModel.viewState, ::renderState)
        observeEvents(viewModel.events, ::onEvent)

        viewModel.fetchMovie(args.movieId)
    }

    private fun renderState(state: MovieDetailViewModel.ScreenState) {
        binding.progressBar.isVisible = state is MovieDetailViewModel.ScreenState.Loading

        if (state is MovieDetailViewModel.ScreenState.Content) {
            val section = Section()
            section.setHeader(MovieDetailHeaderItem(state.data))
            section.add(MovieDetailBodyItem(state.data.overview))
            contentAdapter.add(section)
        }
    }
}

