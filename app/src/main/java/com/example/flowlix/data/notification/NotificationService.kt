package com.example.flowlix.data.notification

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.flowlix.R
import com.example.flowlix.data.MyTime
import com.example.flowlix.data.io
import com.example.flowlix.data.schedule.Schedule
import com.example.flowlix.data.schedule.Scheduler
import com.example.flowlix.presentation.MainActivity
import kotlinx.coroutines.*
import java.sql.Time


const val notificationid = 1
const val channel_id = "channel1"
const val channel_name = "MyChannel"
const val channel_importance = NotificationManager.IMPORTANCE_HIGH

const val serviceRefreshRate: Long = 1

class NotificationService : Service() {

    lateinit var notificationManager : NotificationManager;

    private fun createNotification(): Notification? {
        val pendingIntent = PendingIntent.getActivity(
            application,
            2,
            Intent(application, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Builder(application, "2")
            .setContentTitle("FlowLix")
            .setContentText("Foreground Service")
            //.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher)
            .setAutoCancel(true)
            .setChannelId("2")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        //build notification manager and chanel etc
        val channel = NotificationChannel(channel_id, channel_name, channel_importance)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val channel_foreground = NotificationChannel("2", "forground", channel_importance)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel_foreground)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.d("ns","starting service...")

        var schedule: Schedule = Scheduler.get_or_create_Schedule(applicationContext)
        var nextAlert: Time? = Scheduler.getNextAlarm(schedule, 0)
        val currentTime = MyTime.getTime()
        val delay: Boolean = intent!!.getBooleanExtra("delay", false)

        if(delay){
            Log.d("ns","starting Foreground Service")
            startForeground(1,createNotification())
        }

        if (nextAlert != null) {

            GlobalScope.launch {
                var delay_ms: Long = 0

                if (delay) {
                    delay_ms = 1 * 30000
                    Log.d("ns","delayyyy")
                    nextAlert = Scheduler.getNextAlarm(schedule, -1)
                } else {
                    delay_ms = nextAlert!!.time - currentTime.time
                }

                Log.d(
                    "ns",
                    "alert: ${nextAlert.toString()} and current: ${currentTime.toString()}"
                )

                Log.d("ns","delay set for ${(delay_ms/60000).toString()} min")

                delay(delay_ms)
                sendNotification(schedule, nextAlert!!, delay)
            }
        }
        return Service.START_STICKY
    }

    fun sendNotification(schedule: Schedule, nextAlert: Time, delay: Boolean) {

        val pendingIntent = PendingIntent.getActivity(
            application,
            notificationid,
            Intent(application, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionIntent1 = Intent(application, ActionReceiver::class.java)
        actionIntent1.action = "action1"
        val pendingActionIntent1 = PendingIntent.getBroadcast(
            application,
            0,
            actionIntent1,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionIntent2 = Intent(application, ActionReceiver::class.java)
        actionIntent2.action = "action2"
        actionIntent2.putExtra("nextalert",nextAlert)
        val pendingActionIntent2 = PendingIntent.getBroadcast(
            application,
            0,
            actionIntent2,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        var notification : Notification = Notification()
        if(!delay){
             notification = NotificationCompat.Builder(application, channel_id)
                .setContentTitle("FlowLix")
                .setContentText("Please open App to start sampling or Delay for 5min!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                 .setChannelId(channel_id)
                 .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_launcher, "Open App", pendingActionIntent1)
                .addAction(R.drawable.ic_launcher, "Delay", pendingActionIntent2)
                .build()

        }else{
            notification = NotificationCompat.Builder(application, channel_id)
                .setContentTitle("FlowLix")
                .setContentText("Please open App to start sampling!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(channel_id)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_launcher, "Open App", pendingActionIntent1)
                .build()

        }


        // notificationId is a unique int for each notification that you must define
       notificationManager.notify(notificationid, notification)

        io.appendtofile(
            applicationContext,
            "/notification",
            "alerts_${schedule.date}",
            "${schedule.date},${nextAlert},${Time(System.currentTimeMillis())},$delay"
        )
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}