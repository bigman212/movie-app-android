package ru.redmadrobot.movie_detail

import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.common.data.genre.Genre
import ru.redmadrobot.common.data.movie.entity.MovieDetail
import ru.redmadrobot.common.extensions.context
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.movie_detail.databinding.ItemDetailedMovieHeaderBinding

data class MovieDetailHeaderItem(private val movie: MovieDetail) : BindableItem<ItemDetailedMovieHeaderBinding>() {
    companion object {
        private const val POSTER_CORNERS_RADIUS = 8
    }

    override fun initializeViewBinding(view: View): ItemDetailedMovieHeaderBinding =
        ItemDetailedMovieHeaderBinding.bind(view)

    override fun getId(): Long = movie.id.toLong()

    @SuppressLint("SetTextI18n") // originalText не переводится
    override fun bind(viewBinding: ItemDetailedMovieHeaderBinding, position: Int) {
        Glide.with(viewBinding.root.context)
            .load(NetworkRouter.IMAGES + movie.posterPath)
            .placeholder(R.drawable.ic_movie_list_background_girl)
            .transform(RoundedCorners(POSTER_CORNERS_RADIUS))
            .into(viewBinding.imgMoviePoster)


        viewBinding.tvMovieTitle.text = movie.title
        viewBinding.tvMovieOriginalTitle.text = "${movie.originalTitle} (${movie.releaseDate})"

        val defaultGenresNotFound = viewBinding.context().getString(R.string.genres_not_loaded)
        viewBinding.tvMovieGenres.text = generateGenresString(movie.genres, defaultGenresNotFound)

        viewBinding.tvMovieRatingValue.text = movie.voteAverage.toString()
        viewBinding.tvMovieRatingVotes.text = movie.voteCount.toString()

        viewBinding.tvMovieDurationValue.text = movie.runtime?.toString() ?: "0"
    }

    override fun getLayout(): Int = R.layout.item_detailed_movie_header

    private fun generateGenresString(movieGenres: List<Genre>, defaultString: String): String? {
        return movieGenres
            .map(Genre::name)
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString()
            ?: defaultString
    }
}
