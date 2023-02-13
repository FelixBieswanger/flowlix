package com.example.flowlix.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.example.flowlix.data.io
import com.example.flowlix.presentation.MainActivity
import java.util.concurrent.TimeUnit

class ActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        when (action) {
            "action1" -> {
                val activityIntent = Intent(context, MainActivity::class.java)
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(activityIntent)
            }
            "action2" -> {
                // Call a method that generates a file // means delay of notification
                Log.d("ns","action2")

                var lastalert = intent.getStringExtra("lastalert")

                Log.d("actionreceiver","last alert $lastalert")

                val data = Data.Builder()
                    .putBoolean("delay",true)
                    .putString("lastalert",lastalert)
                    .build()

                val workManager = WorkManager.getInstance(context)
                val constraints = Constraints.Builder().setRequiresBatteryNotLow(false).build()
                val sendNotificationWork = OneTimeWorkRequestBuilder<SendNotificationWorker_delay>()
                    .setInputData(data)
                    .setConstraints(constraints)
                    .setInitialDelay(1*30000, TimeUnit.MILLISECONDS)
                    .build()

                workManager.cancelAllWork()
                workManager.enqueue(sendNotificationWork)

            }
        }
    }
}