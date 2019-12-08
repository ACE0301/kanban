package com.ace.homework2.extentions

import android.content.Context
import com.ace.homework2.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
const val TIME_ZONE_HOURS = 10800
val LOCALE = Locale("ru", "RU")

enum class TimeUnits { SECOND, MINUTE, HOUR, DAY }

fun String.toDate(): Date? {
    val format = SimpleDateFormat(DATE_FORMAT, LOCALE)
    return format.parse(this)
}

fun Date.humanizeDiff(context: Context, date: Date = Date()): String {

    var diffDate = (((this.time - date.time) / 1000) + TIME_ZONE_HOURS).toInt()

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
        else -> {
            this.toLocaleString()
        }
    }
}

fun dateEnding(unit: TimeUnits, count: Int): String {
    return when (unit) {
        TimeUnits.SECOND -> ""
        TimeUnits.MINUTE -> when (count) {
            0, in 5..19 -> "мин."
            1 -> "минуту"
            in 2..4 -> "мин."
            else -> "мин."
        }
        TimeUnits.HOUR -> when (count) {
            0, in 5..19 -> "ч."
            1 -> "час"
            in 2..4 -> "ч."
            else -> "ч."
        }
        TimeUnits.DAY -> when (count) {
            0, in 5..19 -> "дней"
            1 -> "день"
            in 2..4 -> "дня"
            else -> "дня"
        }
    }
}