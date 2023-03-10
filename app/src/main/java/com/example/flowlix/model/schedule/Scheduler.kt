package com.example.flowlix.model.schedule

import android.content.Context
import android.util.Log
import com.example.flowlix.model.data.Schedule
import com.example.flowlix.model.resources.MyTime
import com.example.flowlix.model.io
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

/**
 * The Scheduler object generates a schedule of alerts for the day.
 */
object Scheduler {

    val START_HOUR = 9
    val END_HOUR = 19
    val NUM_ALERTS = 10
    val TIME_BETWEEN_IN_H = 1
    val formatter_date = SimpleDateFormat("yyyy-MM-dd")
    val formatter_time = SimpleDateFormat("HH:mm:ss")
    val GSON = Gson()
    val scheduleType = object : TypeToken<Schedule>() {}.type

    /**
    * Returns the last alert in the schedule.
    *
    * @param schedule The schedule to get the last alert from.
    * @return The time of the last alert, or null if there are no alerts in the schedule.
    */
    fun getLastAlert(schedule: Schedule): Time?{
        val currentTime = MyTime.getTime()
        Log.d("Scheduler.getLastAlert","current time_ ${currentTime.toString()}")
        var timeindex = -1
        for(i in 0..schedule.alerts.size-1){

            val alarmTime = io.parseTimefromString(schedule.alerts[i])
            if(currentTime.compareTo(alarmTime) > 0){
                timeindex = i
            }else{
                break
            }
        }

        if(timeindex == -1){
            return null
        }else{
            return io.parseTimefromString(schedule.alerts[timeindex])
        }
    }

    /**
     * Returns the next alert in the schedule.
     *
     * @param schedule The schedule to get the next alert from.
     * @param offset The offset from the current time to the next alert, in alerts. Defaults to 0.
     * @return The time of the next alert, or null if there are no more alerts in the schedule for the day.
     */
    fun getNextAlarm(schedule: Schedule, offset: Int = 0): Time?{
        val currentTime = MyTime.getTime()
        Log.d("nextalarm","current time_ ${currentTime.toString()}")
        var timeindex = -1
        for(i in 0..schedule.alerts.size-1){

            val alarmTime = io.parseTimefromString(schedule.alerts[i])
            if(currentTime.compareTo(alarmTime) < 0){
                timeindex = i
                break
            }
        }

        Log.d("getNextAlarm","timeindex $timeindex")

        if(timeindex == -1 || (timeindex + offset == -1)){
            //es gibt f??r heute keinen n??chsten mehr
            return null
        }else{
            return io.parseTimefromString(schedule.alerts[timeindex+offset])
        }
    }


    /**
     * Returns the schedule for the day. If a schedule file already exists for the day, it is loaded
     * from disk,otherwise a new schedule is generated and saved to disk.
     *
     * @param context The context of the application.
     * @return The schedule for the day.
     */
    fun get_or_create_Schedule(context: Context): Schedule {

        val mydate = MyTime.getDate()

        val mydateformatted = formatter_date.format(mydate)

        if(io.check_if_file_exists(path="/schedule", fileName = "schedule_$mydateformatted")){
            val filestring = io.read_file(context,path="/schedule", fileName = "schedule_$mydateformatted")
            Log.d("filestring",filestring)
            return GSON.fromJson(filestring, scheduleType)
        }
        return create_and_save_schedule(context,date=mydateformatted)
    }

    /**
     * Generates a list of uniformly distributed samples that add up to y.
     *
     * @param n The number of samples to generate.
     * @param y The sum of the samples.
     * @return The list of samples.
     */
    private fun sampleNTimesSumY(n: Int, y: Double): List<Double> {
        val random = Random()
        val samples = mutableListOf<Double>()
        var sum = 0.0
        for (i in 1..n) {
            // Generate a random number between 0 and 1
            val rand = random.nextDouble()
            // Multiply it by (y - sum) to get a uniformly distributed random number between 0 and (y - sum)
            val sample = rand * (y - sum)
            samples.add(sample)
            sum += sample
        }
        return samples
    }

    private fun create_and_save_schedule(context: Context,date: String): Schedule {

        var listofalerts = generate_schedule(Time(START_HOUR-1,0,0),Time(END_HOUR-1,0,0), NUM_ALERTS, TIME_BETWEEN_IN_H)
        val new_scheudle = Schedule(date = date, alerts = listofalerts)

        val filestring = GSON.toJson(new_scheudle)

        io.appendtofile(context = context,path="/schedule", fileName = "schedule_$date", data = filestring)

        return new_scheudle
    }

    /**
     * Generates a list of alert times for the given time range.
     *
     * @param startTime The start time of the time range.
     * @param endTime The end time of the time range.
     * @param numberofAlerts The number of alerts to generate.
     * @param timebetweenAlerts The time between alerts in hours.
     * @return The list of alert times.
     */
    private fun generate_schedule(startTime: Time, endTime:Time, numberofAlerts: Int, timebetweenAlerts: Int): List<String>{

        val allowed_pause = ((endTime.time-startTime.time)/3600/1000)-numberofAlerts+1
        var pausesamples = sampleNTimesSumY(numberofAlerts,allowed_pause.toDouble())
        pausesamples = pausesamples.shuffled()

        val times = mutableListOf<String>()

        var lastalert = startTime

        for(i in 0..numberofAlerts-1){
            val newalert = Time((lastalert.time + timebetweenAlerts * 3600 * 1000 + (pausesamples[i])*3600*1000).toLong())
            times.add(newalert.toString())
            lastalert= newalert
        }
        return times
    }

}