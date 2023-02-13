package com.example.flowlix.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.flowlix.R
import com.example.flowlix.data.MyTime
import com.example.flowlix.presentation.MainActivity

class SendNotificationWorker_delay(context: Context, workerParameters: WorkerParameters): Worker(context,workerParameters) {


    val notificationid = 1
    val channel_id = "channel1"
    val channel_name = "MyChannel"
    val channel_importance = NotificationManager.IMPORTANCE_HIGH

    override fun doWork(): Result {

        Log.d("SendNotificationWorker_delay","starting Notification Worker Delay @${MyTime.getTime()}")

        val inputData = inputData

        //build notification manager and chanel etc
        val channel = NotificationChannel(channel_id, channel_name, channel_importance)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        val nextalert = inputData.getString("lastalert")


        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            2,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionIntent1 = Intent(applicationContext, MainActivity::class.java)
        actionIntent1.action = "action1"
        val pendingActionIntent1 = PendingIntent.getActivity(
            applicationContext,
            0,
            actionIntent1,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notification = NotificationCompat.Builder(applicationContext, channel_id)
            .setContentTitle("flowlix")
            .setContentText("Reminder for alert $nextalert. Please start the questioning now!")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setChannelId(channel_id)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_launcher, "Open App", pendingActionIntent1)
            .build()


        notificationManager.notify(notificationid, notification)



        return Result.success()
    }
}