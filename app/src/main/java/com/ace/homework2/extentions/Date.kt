package com.ace.homework2.extentions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

fun String.toDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru", "RU"))
    return format.parse(this)
}

fun Date.humanizeDiff(date: Date = Date()): String {

    var diffDate = (((this.time - date.time) / 1000) + 10800).toInt()

    return when (diffDate) {
        in 1 downTo 0 -> "только что"
        in 1..45 -> "через несколько секунд"
        in 45..75 -> "через минуту"
        in 75..(45 * 60) -> "через ${diffDate / 60} ${dateEnding(TimeUnits.MINUTE, diffDate / 60)}"
        in (45 * 60)..(75 * 60) -> "через час"
        in (75 * 60)..(22 * 60 * 60) -> "через ${diffDate / 60 / 60} ${dateEnding(
            TimeUnits.HOUR,
            diffDate / 60 / 60
        )}"
        in (22 * 60 * 60)..(26 * 60 * 60) -> "через день"
        in (26 * 60 * 60)..(360 * 60 * 60) -> "через ${diffDate / 24 / 60 / 60} ${dateEnding(
            TimeUnits.DAY,
            diffDate / 24 / 60 / 60
        )}"
        in -1..0 -> "только что"
        in (360 * 24 * 60 * 60)..Double.POSITIVE_INFINITY.toInt() -> "более чем через год"
        in -1 downTo -45 -> "несколько секунд назад"
        in -45 downTo -75 -> "минуту назад"
        in -75 downTo -(45 * 60) -> "${abs(diffDate / 60)} ${dateEnding(
            TimeUnits.MINUTE,
            abs(diffDate / 60)
        )} назад"
        in -(45 * 60) downTo -(75 * 60) -> "час назад"
        in -(75 * 60) downTo -(22 * 60 * 60) -> "${abs(diffDate / 60 / 60)} ${dateEnding(
            TimeUnits.HOUR,
            abs(diffDate / 60 / 60)
        )} назад"
        in -(22 * 60 * 60) downTo -(26 * 60 * 60) -> "день назад"
        in -(26 * 60 * 60) downTo -(360 * 60 * 60) -> "${abs(diffDate / 24 / 60 / 60)} ${dateEnding(
            TimeUnits.DAY,
            abs(diffDate / 24 / 60 / 60)
        )} назад"
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

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value: Long): String {
        return Utils.plurals(value, timeunit = this)
    }
}

object Utils {
    fun plurals(i: Long, timeunit: TimeUnits): String {

        val j = i % 10

        return when (j) {
            in 1..1 -> when (timeunit) {
                TimeUnits.SECOND -> "$i секунду"
                TimeUnits.MINUTE -> "$i минуту"
                TimeUnits.HOUR -> "$i час"
                TimeUnits.DAY -> "$i день"
            }
            in 2..4 -> when (timeunit) {
                TimeUnits.SECOND -> "$i секунды"
                TimeUnits.MINUTE -> "$i минуты"
                TimeUnits.HOUR -> "$i часа"
                TimeUnits.DAY -> "$i дня"
            }
            else -> when (timeunit) {
                TimeUnits.SECOND -> "$i секунд"
                TimeUnits.MINUTE -> "$i минут"
                TimeUnits.HOUR -> "$i часов"
                TimeUnits.DAY -> "$i дней"
            }
        }
    }
}