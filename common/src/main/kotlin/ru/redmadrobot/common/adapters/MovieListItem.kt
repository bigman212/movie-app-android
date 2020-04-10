package ru.redmadrobot.common.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.common.R
import ru.redmadrobot.common.data.genre.Genre
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.databinding.ItemMovieAsListBinding
import ru.redmadrobot.common.extensions.year
import ru.redmadrobot.core.network.NetworkRouter

data class MovieListItem(private val movie: Movie) : BindableItem<ItemMovieAsListBinding>() {
    companion object {
        private const val POSTER_CORNERS_RADIUS = 8
    }

    override fun initializeViewBinding(view: View): ItemMovieAsListBinding {
        return ItemMovieAsListBinding.bind(view)
    }

    override fun getId(): Long = movie.id.toLong()

    @SuppressLint("SetTextI18n") // originalText не переводится
    override fun bind(viewBinding: ItemMovieAsListBinding, position: Int) {
        Glide.with(viewBinding.root.context)
            .load(NetworkRouter.IMAGES + movie.posterPath)
            .placeholder(R.drawable.ic_movie_list_background_girl)
            .transform(RoundedCorners(POSTER_CORNERS_RADIUS))
            .into(viewBinding.imgMoviePoster)


        viewBinding.tvMovieTitle.text = movie.title
        viewBinding.tvMovieTitleSmall.text = "${movie.originalTitle} (${movie.releaseDate.year()})"

        val defaultGenresNotFound = viewBinding.context().getString(R.string.genres_not_loaded)
        viewBinding.tvMovieGenre.text = generateGenresString(movie.genres, defaultGenresNotFound)

        viewBinding.tvMovieRating.text = movie.voteAverage.toString()
        viewBinding.tvMovieVoteCount.text = movie.voteCount.toString()

        viewBinding.tvMovieDuration.text = generateDurationString(viewBinding.context(), movie.runtime)
    }

    override fun getLayout(): Int = R.layout.item_movie_as_list

    private fun generateGenresString(movieGenres: List<Genre>, defaultString: String): String? {
        return movieGenres
            .map(Genre::name)
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString()
            ?: defaultString
    }

    private fun generateDurationString(context: Context, duration: Int): String {
        if (movie.runtime == 0) {
            return context.getString(R.string.item_movie_duration_unknown)
        }
        return context.getString(R.string.item_movie_duration_format, duration)
    }

    private fun ItemMovieAsListBinding.context(): Context = root.context
}
