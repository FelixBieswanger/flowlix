package com.example.flowlix.data

import android.os.Debug
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date

object MyTime {

    val formatter = SimpleDateFormat("yyyy-MM-dd")

    private val DEBUG = false


    fun getTime(): Time{
        if(DEBUG){
            return Time(10,15,20)
        }else{
            val t=  Time(System.currentTimeMillis())
            return Time(t.hours,t.minutes,t.seconds)
        }


    }

    fun getDate(): Date{
        if(DEBUG){
            val mydate = "2023-01-05"
            return formatter.parse(mydate)
        }else{
            return Date(System.currentTimeMillis())
        }

    }
}