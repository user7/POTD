package com.geekbrains.potd.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class NasaDate {
    private var dayShift = 0

    fun adjust(delta: Int): Boolean {
        val newDayShift = dayShift + delta
        if (newDayShift <= 0 && newDayShift != dayShift) {
            dayShift = newDayShift
            return true
        }
        return false
    }

    fun reset(): Boolean {
        if (dayShift != 0) {
            dayShift = 0
            return true
        }
        return false
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("EST")
    }

    fun format(): String {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.add(Calendar.DAY_OF_MONTH, dayShift)
        return dateFormat.format(cal.time)
    }

    fun setFromApiDate(date: String) {
        dayShift = 0
        val curCal = Calendar.getInstance(Locale.getDefault())
        curCal.time = dateFormat.parse(format())!!

        val cal = Calendar.getInstance(Locale.getDefault())
        cal.time = dateFormat.parse(date)!!

        dayShift =
            ((cal.timeInMillis - curCal.timeInMillis + 0f) / (24 * 60 * 60 * 1000)).roundToInt()
    }
}