package com.example.flowlix.data

import android.os.Debug
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Object containing helper functions related to time.
 */
object MyTime {
    /**
     * SimpleDateFormat object to format dates.
     */
    val formatter = SimpleDateFormat("yyyy-MM-dd")

    /**
     * Debug flag.
     */
    private val DEBUG = false

    /**
     * Returns the current time if in production mode or a hardcoded time if in debug mode.
     * @return the current time
     */
    fun getTime(): Time {
        if (DEBUG) {
            return Time(10, 15, 20)
        } else {
            val t = Time(System.currentTimeMillis())
            return Time(t.hours, t.minutes, t.seconds)
        }
    }

    /**
     * Returns the current date if in production mode or a hardcoded date if in debug mode.
     * @return the current date
     */
    fun getDate(): Date {
        if (DEBUG) {
            val mydate = "2023-01-05"
            return formatter.parse(mydate)
        } else {
            return Date(System.currentTimeMillis())
        }
    }
}
