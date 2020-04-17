package ru.redmadrobot.common.data.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieAccountState(
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "favorite")
    val isInFavorites: Boolean,

//    @field:Json(name = "rated")
//    val ratedValue: RatedValue?,

    @field:Json(name = "watchlist")
    val isInWatchList: Boolean
)

data class RatedValue(
    @field:Json(name = "value")
    val value: Int
)

//@JsonQualifier
//@Retention(AnnotationRetention.RUNTIME)
//@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
//annotation class ForceToInt
//
//internal class ForceToIntJsonAdapter {
//    @ToJson
//    fun toJson(@ForceToInt i: Int):  {
//        return i
//    }
//
//    @FromJson
//    @ForceToInt
//    fun fromJson(reader: JsonReader): Int {
//        return when (reader.peek()) {
//            JsonReader.Token.NUMBER -> reader.nextInt()
//            JsonReader.Token.BOOLEAN -> if (reader.nextBoolean()) 1 else 0
//            else -> {
//                reader.skipValue() // or throw
//                0
//            }
//        }
//    }
//}
