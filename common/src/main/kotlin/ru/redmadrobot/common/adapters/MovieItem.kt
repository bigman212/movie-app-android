package ru.redmadrobot.common.adapters

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.common.R
import ru.redmadrobot.common.data.genre.Genre

abstract class MovieItem<T : ViewBinding> : BindableItem<T>() {
    companion object {
        internal const val POSTER_CORNERS_RADIUS = 8
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
}
