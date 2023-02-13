package com.example.flowlix.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.example.flowlix.presentation.MainActivity
import java.util.concurrent.TimeUnit


/**
 * [ActionReceiver] is a [BroadcastReceiver] that listens to specific actions.
 *
 * The class implements the `onReceive` method to handle the actions received.
 */
class ActionReceiver : BroadcastReceiver() {

    /**
     * The `onReceive` method is called when an action is received.
     *
     * @param context [Context] of the application.
     * @param intent [Intent] containing the action received.
     */
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        when (action) {
            // Start MainActivity when "action1" is received
            "action1" -> {
                val activityIntent = Intent(context, MainActivity::class.java)
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(activityIntent)
            }

            // User Selected Delay, send notification in 5 min
            "action2" -> {

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
                    .setInitialDelay(5*60000, TimeUnit.MILLISECONDS)
                    .build()

                workManager.cancelAllWork()
                workManager.enqueue(sendNotificationWork)

            }
        }
    }
}