package ru.redmadrobot.common.data.genre

import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.Reusable
import io.reactivex.Completable
import timber.log.Timber
import javax.inject.Inject

@Reusable
class GenresRepository @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val genreService: GenreApi
) {
    private fun prefsKeyFromId(genreId: Long) = "genre_$genreId"

    fun fetchGenresAndSave(): Completable {
        return genreService.allMovieGenres()
            .flatMapCompletable { genreResponse ->
                Completable.fromAction { this.saveGenres(genreResponse.result) }
            }
    }

    private fun saveGenres(genres: List<Genre>) {
        genres.forEach { genreToSave ->
            val prefsKey = prefsKeyFromId(genreToSave.id)
            val prefsValue = genreToSave.name

            if (!sharedPrefs.contains(prefsKey)) {
                sharedPrefs.edit {
                    putString(prefsKey, prefsValue)
                }

                Timber.d("$genreToSave saved to sharedPrefs")
            }
        }
    }

    fun genreById(id: Long): Genre? {
        val prefsKey = prefsKeyFromId(id)
        val genreName: String? = sharedPrefs.getString(prefsKey, null)

        return genreName?.let { name -> Genre(id, name) }
    }
}
