package com.ace.homework2.utils

import android.content.Context
import com.ace.homework2.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class DateUtil(val context: Context) {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val TIME_ZONE_HOURS = 10800
        val LOCALE = Locale("ru", "RU")
    }

    enum class TimeUnits { SECOND, MINUTE, HOUR, DAY }

    fun humanizeDiff(date: String, dateNow: Date = Date()): String {
        val format = SimpleDateFormat(
            DATE_FORMAT,
            LOCALE
        )
        val date = format.parse(date)

        var diffDate = (((date.time - dateNow.time) / 1000) + TIME_ZONE_HOURS).toInt()

        return when (diffDate) {
            in -1..0 -> context.getString(R.string.right_now)
            in -1 downTo -45 -> context.getString(R.string.some_seconds_ago)
            in -45 downTo -75 -> context.getString(R.string.minute_ago)
            in -75 downTo -(45 * 60) -> context.getString(
                R.string.some_time_ago,
                abs(diffDate / 60), dateEnding(
                    TimeUnits.MINUTE, abs(diffDate / 60)
                )
            )
            in -(45 * 60) downTo -(75 * 60) -> context.getString(R.string.hour_ago)
            in -(75 * 60) downTo -(22 * 60 * 60) -> context.getString(
                R.string.some_time_ago,
                abs(diffDate / 60 / 60), dateEnding(
                    TimeUnits.HOUR,
                    abs(diffDate / 60 / 60)
                )
            )
            in -(22 * 60 * 60) downTo -(26 * 60 * 60) -> context.getString(R.string.day_ago)
            in -(26 * 60 * 60) downTo -(360 * 60 * 60) -> context.getString(
                R.string.some_time_ago,
                abs(diffDate / 24 / 60 / 60), dateEnding(
                    TimeUnits.DAY,
                    abs(diffDate / 24 / 60 / 60)
                )
            )
            else -> ""
        }
    }

    private fun dateEnding(unit: TimeUnits, count: Int): String {
        return when (unit) {
            TimeUnits.SECOND -> ""
            TimeUnits.MINUTE -> when (count) {
                0, in 5..19 -> context.getString(R.string.min)
                1 -> context.getString(R.string.min)
                in 2..4 -> context.getString(R.string.min)
                else -> context.getString(R.string.min)
            }
            TimeUnits.HOUR -> when (count) {
                0, in 5..19 -> context.getString(R.string.hour)
                1 -> context.getString(R.string.hour)
                in 2..4 -> context.getString(R.string.hour)
                else -> context.getString(R.string.hour)
            }
            TimeUnits.DAY -> when (count) {
                0, in 5..19 -> context.getString(R.string.day)
                1 -> context.getString(R.string.day)
                in 2..4 -> context.getString(R.string.day)
                else -> context.getString(R.string.day)
            }
        }
    }
}