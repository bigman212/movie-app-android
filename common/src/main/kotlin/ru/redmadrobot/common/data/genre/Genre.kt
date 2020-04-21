package ru.redmadrobot.common.data.genre

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.redmadrobot.persist.entities.GenreDb

@JsonClass(generateAdapter = true)
data class Genre(
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "name")
    val name: String
) {
    fun toGenreDb(): GenreDb {
        return GenreDb(
            genreId = this.id,
            name = this.name
        )
    }

    companion object {
        fun fromGenreDb(genreDb: GenreDb): Genre {
            return Genre(
                id = genreDb.genreId,
                name = genreDb.name
            )
        }
    }
}

