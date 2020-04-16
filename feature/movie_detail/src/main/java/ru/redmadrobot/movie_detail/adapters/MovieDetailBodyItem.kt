package ru.redmadrobot.movie_detail.adapters

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.common.extensions.context
import ru.redmadrobot.movie_detail.R
import ru.redmadrobot.movie_detail.databinding.ItemDetailedMovieDescriptionBinding

data class MovieDetailBodyItem(private val description: String?) : BindableItem<ItemDetailedMovieDescriptionBinding>() {
    override fun initializeViewBinding(view: View): ItemDetailedMovieDescriptionBinding {
        return ItemDetailedMovieDescriptionBinding.bind(view)
    }

    override fun getId(): Long = description.hashCode().toLong()

    override fun bind(viewBinding: ItemDetailedMovieDescriptionBinding, position: Int) {
        viewBinding.tvMovieDescription.text = description
            ?: viewBinding.context.getString(R.string.item_detailed_movie_no_overview)
    }

    override fun getLayout(): Int = R.layout.item_detailed_movie_description

}

