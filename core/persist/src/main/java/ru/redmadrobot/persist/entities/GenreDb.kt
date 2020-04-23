package ru.redmadrobot.persist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = GenreDb.TABLE_NAME)
data class GenreDb(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val genreId: Long,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String
) {
    companion object {
        const val TABLE_NAME = "genres"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }
}
