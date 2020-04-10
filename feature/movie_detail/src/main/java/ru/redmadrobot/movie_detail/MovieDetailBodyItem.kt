package ru.redmadrobot.movie_detail

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.movie_detail.databinding.ItemDetailedMovieDescriptionBinding

data class MovieDetailBodyItem(private val description: String) : BindableItem<ItemDetailedMovieDescriptionBinding>() {
    override fun initializeViewBinding(view: View): ItemDetailedMovieDescriptionBinding {
        return ItemDetailedMovieDescriptionBinding.bind(view)
    }

    override fun getId(): Long = description.hashCode().toLong()

    override fun bind(viewBinding: ItemDetailedMovieDescriptionBinding, position: Int) {
        viewBinding.tvMovieDescription.text = description
    }

    override fun getLayout(): Int = R.layout.item_detailed_movie_description

}

