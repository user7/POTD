package com.geekbrains.potd.utils

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

    fun format(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, dayShift)
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fmt.timeZone = TimeZone.getTimeZone("EST")
        return fmt.format(cal.time)
    }
}