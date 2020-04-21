package ru.redmadrobot.persist.conventers

import androidx.room.TypeConverter
import java.util.Calendar

class LongToCalendarConverter {
    @TypeConverter
    fun fromLong(value: Long?): Calendar? {
        return value?.let { calendarInLong ->
            Calendar.getInstance().apply { timeInMillis = calendarInLong }
        }
    }

    @TypeConverter
    fun toCalendar(calendar: Calendar?): Long? = calendar?.timeInMillis
}
