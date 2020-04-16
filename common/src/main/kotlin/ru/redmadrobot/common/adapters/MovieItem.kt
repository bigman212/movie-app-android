package ru.redmadrobot.common.adapters

import android.content.Context
import android.content.res.Resources
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.common.R
import ru.redmadrobot.common.data.genre.Genre
import ru.redmadrobot.common.extensions.year
import java.util.Calendar

abstract class MovieItem<T : ViewBinding> : BindableItem<T>() {
    protected fun getPosterCornerRadius(resources: Resources): Int {
        return resources.getInteger(R.integer.movie_poster_corner_radius)
    }

    protected fun generateOriginalTitleString(context: Context, originalTitle: String, releaseDate: Calendar?): String {
        return if (releaseDate == null) {
            context.getString(R.string.item_movie_original_title_unknown, originalTitle)
        } else {
            context.getString(R.string.item_movie_original_title, originalTitle, releaseDate.year())
        }
    }

    protected fun generateGenresString(context: Context, movieGenres: List<Genre>): String {
        return movieGenres
            .map(Genre::name)
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString()
            ?: context.getString(R.string.genres_not_loaded)
    }

    protected open fun generateDurationString(context: Context, duration: Int): String {
        if (duration == 0) {
            return context.getString(R.string.item_movie_duration_unknown)
        }
        return context.getString(R.string.item_movie_duration_format, duration)
    }
}
