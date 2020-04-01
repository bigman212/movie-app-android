package ru.redmadrobot.movie_list.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.redmadrobot.common.extensions.year
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.movie_list.R
import ru.redmadrobot.movie_list.databinding.ItemMovieAsListBinding

class MovieViewHolder(private val itemViewBinding: ItemMovieAsListBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {

    @SuppressLint("SetTextI18n") // оригинальный текст не переводится
    fun bind(movie: Movie) {
        Glide.with(itemView.context)
            .load(NetworkRouter.IMAGES + movie.posterPath)
            .placeholder(R.drawable.ic_movie_list_background_girl)
            .into(itemViewBinding.imgMoviePoster)

        itemViewBinding.tvMovieTitle.text = movie.title
        itemViewBinding.tvMovieTitleSmall.text = "${movie.originalTitle} (${movie.releaseDate.year()})"

        itemViewBinding.tvMovieGenre.text = movie.genreIds.toString()

        itemViewBinding.tvMovieRating.text = movie.voteAverage.toString()
        itemViewBinding.tvMovieVoteCount.text = movie.voteCount.toString()
    }
}
