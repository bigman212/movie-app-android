package ru.redmadrobot.common.utils

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    fun dateStrToCalendar(date: String, dateFormat: String = "yyyy-MM-dd"): Calendar {
        val format = SimpleDateFormat(dateFormat, Locale.getDefault())
        val parsedDate = try {
            format.parse(date)
        } catch (ex: ParseException) {
            Timber.i(ex)
            Date()
        }

        return Calendar.getInstance().apply { time = parsedDate }
    }
}
