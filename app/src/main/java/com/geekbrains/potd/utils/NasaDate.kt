package com.geekbrains.potd.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

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
}