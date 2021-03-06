package ru.redmadrobot.common.adapters

import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.redmadrobot.common.R
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.databinding.ItemMovieAsListBinding
import ru.redmadrobot.common.extensions.context
import ru.redmadrobot.common.extensions.resources
import ru.redmadrobot.core.network.NetworkRouter

data class MovieListItem(private val movie: Movie, val onClickListener: (item: Movie) -> Unit) :
    MovieItem<ItemMovieAsListBinding>() {

    override fun initializeViewBinding(view: View): ItemMovieAsListBinding = ItemMovieAsListBinding.bind(view)

    override fun getId(): Long = movie.id.toLong()

    override fun getLayout(): Int = R.layout.item_movie_as_list

    @SuppressLint("SetTextI18n") // originalText не переводится
    override fun bind(viewBinding: ItemMovieAsListBinding, position: Int) {
        val posterCorners = RoundedCorners(getPosterCornerRadius(viewBinding.resources))
        Glide.with(viewBinding.root.context)
            .load(NetworkRouter.IMAGES + movie.posterPath)
            .placeholder(R.drawable.ic_movie_list_background_girl)
            .transform(posterCorners)
            .into(viewBinding.imgMoviePoster)


        viewBinding.tvMovieTitle.text = movie.title
        viewBinding.tvMovieTitleSmall.text = generateOriginalTitleString(
            viewBinding.context, movie.originalTitle, movie.releaseDate
        )

        viewBinding.tvMovieGenre.text = generateGenresString(viewBinding.context, movie.genres)

        viewBinding.tvMovieRating.text = movie.voteAverage.toString()
        viewBinding.tvMovieVoteCount.text = movie.voteCount.toString()

        viewBinding.tvMovieDuration.text = generateDurationString(viewBinding.context, movie.runtime)

        viewBinding.root.setOnClickListener { onClickListener(movie) }
    }
}
