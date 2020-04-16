package ru.redmadrobot.core.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class AsCalendar

class MovieReleaseDateAdapter {
    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
    }

    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    @ToJson
    fun toJson(writer: JsonWriter, @AsCalendar value: Calendar?) {
        value?.let {
            val dateValue = calendarToString(it)
            writer
                .name("release_date")
                .value(dateValue)
        }
    }

    private fun calendarToString(calendar: Calendar): String = dateFormat.format(calendar.time)

    @AsCalendar
    @FromJson
    fun fromJson(reader: JsonReader): Calendar {
        val date = reader.nextString()
        return dateStringToCalendar(date)
    }

    private fun dateStringToCalendar(date: String): Calendar {
        val parsedDate = dateFormat.parse(date)

        return Calendar.getInstance()
            .apply { time = parsedDate ?: Date() }
    }
}
