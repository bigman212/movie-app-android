package ru.redmadrobot.common.adapters

import android.content.Context
import android.content.res.Resources
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.common.R
import ru.redmadrobot.common.data.genre.Genre

abstract class MovieItem<T : ViewBinding> : BindableItem<T>() {
    protected fun getPosterCornerRadius(resources: Resources): Int {
        return resources.getInteger(R.integer.movie_poster_corner_radius)
    }

    protected fun generateGenresString(movieGenres: List<Genre>, defaultString: String): String {
        return movieGenres
            .map(Genre::name)
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString()
            ?: defaultString
    }

    protected fun generateDurationString(context: Context, duration: Int): String {
        if (duration == 0) {
            return context.getString(R.string.item_movie_duration_unknown)
        }
        return context.getString(R.string.item_movie_duration_format, duration)
    }

    protected val ViewBinding.context: Context
        get() = root.context

    protected val ViewBinding.resources: Resources
        get() = context.resources
}
