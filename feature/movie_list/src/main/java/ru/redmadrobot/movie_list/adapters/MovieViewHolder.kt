package ru.redmadrobot.movie_list.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.redmadrobot.common.data.entity.Genre
import ru.redmadrobot.common.extensions.year
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.movie_list.R
import ru.redmadrobot.movie_list.data.entity.Movie
import ru.redmadrobot.movie_list.databinding.ItemMovieAsListBinding

class MovieViewHolder(private val itemViewBinding: ItemMovieAsListBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {
    companion object {
        private const val POSTER_CORNERS_RADIUS = 8
    }

    @SuppressLint("SetTextI18n") // Movie::originalTitle не переводится
    fun bind(movie: Movie) {
        Glide.with(itemView.context)
            .load(NetworkRouter.IMAGES + movie.posterPath)
            .placeholder(R.drawable.ic_movie_list_background_girl)
            .transform(RoundedCorners(POSTER_CORNERS_RADIUS))
            .into(itemViewBinding.imgMoviePoster)

        itemViewBinding.tvMovieTitle.text = movie.title
        itemViewBinding.tvMovieTitleSmall.text = "${movie.originalTitle} (${movie.releaseDate.year()})"

        val runtimeAsString = if (movie.runtime == 0) "Продолжительность неизвестна" else "${movie.runtime} min."
        itemViewBinding.tvMovieDuration.text = runtimeAsString

        itemViewBinding.tvMovieGenre.text = generateGenresString(movie.genres)

        itemViewBinding.tvMovieRating.text = movie.voteAverage.toString()
        itemViewBinding.tvMovieVoteCount.text = movie.voteCount.toString()
    }

    private fun generateGenresString(movieGenres: List<Genre>): String? {
        return movieGenres
            .map(Genre::name)
            .takeIf { it.isNotEmpty() }
            ?.reduce { acc, s -> "$acc, $s" }
            ?: itemView.context.getString(R.string.genres_not_loaded)
    }
}
