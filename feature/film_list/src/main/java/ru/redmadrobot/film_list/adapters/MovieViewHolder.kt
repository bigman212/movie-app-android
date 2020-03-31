package ru.redmadrobot.film_list.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import ru.redmadrobot.common.extensions.year
import ru.redmadrobot.film_list.databinding.ItemMovieAsListBinding

class MovieViewHolder(private val itemViewBinding: ItemMovieAsListBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {

    @SuppressLint("SetTextI18n") // оригинальный текст не переводится
    fun bind(movie: Movie) {
        itemViewBinding.tvMovieTitle.text = movie.title
        itemViewBinding.tvMovieTitleSmall.text = "${movie.originalTitle} (${movie.releaseDate.year()})"

        itemViewBinding.tvMovieGenre.text = movie.genreIds.toString()
        itemViewBinding.tvMovieRating.text = movie.voteAverage.toString()
        itemViewBinding.tvMovieVoteCount.text = movie.voteCount.toString()
    }
}
