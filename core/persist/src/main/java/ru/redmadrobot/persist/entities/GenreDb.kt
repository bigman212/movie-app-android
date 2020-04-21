package ru.redmadrobot.persist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "genres")
data class GenreDb(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val uid: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "genre_id") // from server
    val genreId: Long,

    @ColumnInfo(name = "name")
    val name: String
)
