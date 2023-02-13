/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.flowlix.presentation

import android.Manifest
import android.app.NotificationManager
import android.icu.util.Calendar.SECOND
import android.icu.util.MeasureUnit.SECOND
import android.os.Bundle
import android.text.format.Time.SECOND

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer

import androidx.wear.compose.material.MaterialTheme
import androidx.work.*

import com.example.flowlix.data.FlowViewModel
import com.example.flowlix.data.MyTime

import com.example.flowlix.data.notification.SendNotificationWorker
import com.example.flowlix.data.schedule.Schedule
import com.example.flowlix.data.schedule.Scheduler

import com.example.flowlix.presentation.theme.FlowLixTheme
import java.sql.Time
import java.util.concurrent.TimeUnit



/**
 * MainActivity
 * The main activity of the application. This activity contains the user interface that is displayed
 * to the user. It is responsible for initializing the view model, registering for activity results,
 * and setting the content of the activity using the Compose UI framework.
 */
class MainActivity : ComponentActivity() {


    private val viewModel: FlowViewModel by viewModels()


    /**
     * onStart
     * Called when the activity is starting. This method sets the next alert for the user,
     * schedules a work request for the notification worker, and calls the checkifalert and
     * setNextAlarm methods on the view model.
     */
    override fun onStart() {
        super.onStart()


        var schedule: Schedule = Scheduler.get_or_create_Schedule(applicationContext)
        var nextAlert: Time? = Scheduler.getNextAlarm(schedule, 0)

        if (nextAlert != null) {

            val delay = nextAlert.time - MyTime.getTime().time

            Log.d("worker", "delay set for ${(delay / 60000).toString()} min")

            val constraints = Constraints.Builder().setRequiresBatteryNotLow(false).build()
            val sendNotificationWork = OneTimeWorkRequestBuilder<SendNotificationWorker>()
                .setConstraints(constraints)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            val workManager = WorkManager.getInstance(this)

            workManager.cancelAllWork()
            workManager.enqueue(sendNotificationWork)

        }



        viewModel.checkifalert()
        viewModel.setNextAlarm()


    }

    /**
    * onCreate method is the first callback that is called when the Activity is created.
    * In this method, the content view is set and the required permissions are requested.
    * @param savedInstanceState A Bundle containing the data most recently supplied in [onSaveInstanceState].
    * Note: If this method is called after [onStart], it will be passed the data stored in [onSaveInstanceState].
    * The following permissions are requested in this method: READ_EXTERNAL_STORAGE & WRITE_EXTERNAL_STORAGE
    * If both of these permissions are granted, the method sets the content view to the FlowScreen which is
    * defined in the FlowLixTheme composable function.
    * If any of these permissions are denied, the method sets the content view to the [PermissionDeniedScreen]
    * composable function.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Log.d("permission", "file accsess allowed")

                    setContent {
                        setContent {
                            FlowLixTheme {
                                /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
                                 * version of LazyColumn for wear devices with some added features. For more information,
                                 * see d.android.com/wear/compose.
                                 */
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colors.background),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    FlowScreen(flowViewModel = viewModel)
                                }
                            }
                        }
                    }
                } else {
                    setContent {
                        setContent {
                            FlowLixTheme {
                                /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
                         * version of LazyColumn for wear devices with some added features. For more information,
                         * see d.android.com/wear/compose.
                         */
                                PermissionDeniedScreen()
                            }
                        }
                    }
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)


    }
}