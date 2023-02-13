/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.flowlix.presentation

import android.Manifest
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

import com.example.flowlix.data.notification.NotificationService
import com.example.flowlix.data.notification.SendNotificationWorker
import com.example.flowlix.data.schedule.Schedule
import com.example.flowlix.data.schedule.Scheduler

import com.example.flowlix.presentation.theme.FlowLixTheme
import java.sql.Time
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {


    private val viewModel: FlowViewModel by viewModels()

    override fun onStart() {
        super.onStart()

        /*


        Log.d("mytime","current time ${MyTime.getTime()}")
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)
        val serviceClass = NotificationService::class.java
        val isRunning = services.any { service -> service.service.className == serviceClass.name }

        if(isRunning) {

            Log.d("ns", "stopping service...")
            val stopServiceIntent = Intent(this, NotificationService::class.java)
            stopService(stopServiceIntent)
        }


        val serviceIntent = Intent(this,NotificationService::class.java)
        serviceIntent.putExtra("delay",false)
        startService(serviceIntent)


         */

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