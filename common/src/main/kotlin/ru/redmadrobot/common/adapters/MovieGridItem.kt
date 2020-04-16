package ru.redmadrobot.common.adapters

import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.redmadrobot.common.R
import ru.redmadrobot.common.data.movie.entity.Movie
import ru.redmadrobot.common.databinding.ItemMovieAsGridBinding
import ru.redmadrobot.core.network.NetworkRouter

data class MovieGridItem(private val movie: Movie, private val onClickListener: (item: Movie) -> Unit) :
    MovieItem<ItemMovieAsGridBinding>() {

    override fun initializeViewBinding(view: View): ItemMovieAsGridBinding = ItemMovieAsGridBinding.bind(view)

    override fun getId(): Long = movie.id.toLong()

    override fun getLayout(): Int = R.layout.item_movie_as_grid

    @SuppressLint("SetTextI18n") // originalText не переводится
    override fun bind(viewBinding: ItemMovieAsGridBinding, position: Int) {
        val posterCorners = RoundedCorners(getPosterCornerRadius(viewBinding.resources))
        Glide.with(viewBinding.context)
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
