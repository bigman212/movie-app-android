package ru.redmadrobot.movie_detail

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.common.extensions.viewBinding
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

    }

    private fun initViewModel() {

    }

}

