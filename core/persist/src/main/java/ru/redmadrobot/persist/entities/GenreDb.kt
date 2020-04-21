package ru.redmadrobot.persist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreDb(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val genreId: Long,

    @ColumnInfo(name = "name")
    val name: String
)
