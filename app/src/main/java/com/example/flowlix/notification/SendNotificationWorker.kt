package com.example.flowlix.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.flowlix.R
import com.example.flowlix.model.resources.MyTime
import com.example.flowlix.model.schedule.Schedule
import com.example.flowlix.model.schedule.Scheduler
import com.example.flowlix.presentation.MainActivity

/**
* The SendNotificationWorker class is a worker class that is used to send a notification to the user.
* It extends the [Worker] class and implements the doWork method, which is called when the worker is executed.
* @param context: A context that is passed to the worker.
* @param workerParameters: Parameters that are passed to the worker.
* @property channel_id: A constant string that defines the notification channel ID.
* @property channel_name: A constant string that defines the notification channel name.
* @property channel_importance: A constant integer that defines the notification channel importance.
* @property notificationid: A constant integer that defines the notification ID.
*/

class SendNotificationWorker(context: Context, workerParameters: WorkerParameters): Worker(context,workerParameters) {


    val notificationid = 1
    val channel_id = "channel1"
    val channel_name = "MyChannel"
    val channel_importance = NotificationManager.IMPORTANCE_HIGH

    /**
    * The doWork method is called when the worker is executed. It builds a notification, sets up the notification channel,
    * creates the notification, and sends it to the user.
    * @return Result: The result of the worker execution, which is either success or failure.
     */
    override fun doWork(): Result {

        Log.d("SendNotificationWorker","starting Notification Worker @${MyTime.getTime()}")


        //build notification manager and chanel etc
        val channel = NotificationChannel(channel_id, channel_name, channel_importance)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        var schedule: Schedule = Scheduler.get_or_create_Schedule(applicationContext)
        var lastalert = Scheduler.getNextAlarm(schedule, -1)


        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificationid,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionIntent1 = Intent(applicationContext, ActionReceiver::class.java)
        actionIntent1.action = "action1"
        val pendingActionIntent1 = PendingIntent.getBroadcast(
            applicationContext,
            0,
            actionIntent1,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionIntent2 = Intent(applicationContext, ActionReceiver::class.java)
        actionIntent2.action = "action2"
        actionIntent2.putExtra("lastalert",lastalert.toString())

        val pendingActionIntent2 = PendingIntent.getBroadcast(
            applicationContext,
            0,
            actionIntent2,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(applicationContext, channel_id)
            .setContentTitle("flowlix")
            .setContentText("Please open App to start sampling or Delay for 5min!")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setChannelId(channel_id)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_launcher, "Open App", pendingActionIntent1)
            .addAction(R.drawable.ic_launcher, "Delay", pendingActionIntent2)
            .build()


        notificationManager.notify(notificationid, notification)



        return Result.success()
    }
}