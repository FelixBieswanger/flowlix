package com.example.flowlix.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.flowlix.model.data.FlowUIState
import com.example.flowlix.model.io
import com.example.flowlix.model.resources.StringProvider
import com.example.flowlix.model.schedule.Schedule
import com.example.flowlix.model.schedule.Scheduler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Time


/**
 * FlowViewModel is the view model class for the Flow UI.
 * It maintains the current state of the UI and provides functions for updating it.
 * It also provides functions for handling the flow of the survey and saving the results.
 *
 * @property context The context of the application
 * @property _uiState The mutable state flow of the UI
 * @property uiState The state flow of the UI
 * @property schedule The Schedule object for the current survey
 * @property lastAlertTime The time of the last alert for the current survey
 *
 * @constructor Creates a new instance of FlowViewModel with the specified application context
 */
class FlowViewModel(app: Application) : AndroidViewModel(app) {

    val context = getApplication<Application>().applicationContext

    private val _uiState = MutableStateFlow(FlowUIState())
    val uiState: StateFlow<FlowUIState> = _uiState.asStateFlow()

    private var schedule: Schedule
    private var lastAlertTime: Time? = null


    init {
        this.schedule = Scheduler.get_or_create_Schedule(context)
    }


    /**
     * Set the next alarm for the current survey.
     * The next alarm is obtained using the Scheduler.getNextAlarm method.
     * If a next alarm is found, it is set in the _uiState.
     * If there is no next alarm, the nextAlarm property in the _uiState is set to "No more pending".
     */
    fun setNextAlarm() {
        val nextAlarm = Scheduler.getNextAlarm(this.schedule)

        Log.d("getnextAlarm", nextAlarm.toString())
        if (nextAlarm == null) {
            _uiState.value = _uiState.value.copy(nextAlarm = "No more pending")
        } else {
            _uiState.value = _uiState.value.copy(nextAlarm = nextAlarm.toString())

        }
    }

    /**
     * Check if an alert has been triggered for the current survey.
     * If an alert has been triggered, the lastAlertTime is obtained from the Scheduler.getLastAlert method.
     * If a lastAlertTime is found, it is checked if a results file for that alert time exists.
     * If the file exists, nothing happens.
     * If the file does not exist, the isEvent property in the _uiState is set to true.
     */
    fun checkifalert() {

        this.schedule = Scheduler.get_or_create_Schedule(context)

        this.lastAlertTime = Scheduler.getLastAlert(this.schedule)

        Log.d("checkifalert", "lastalerttime $lastAlertTime")

        if (this.lastAlertTime != null) {

            Log.d("checkalert", "check file results_${schedule!!.date}_$lastAlertTime.csv")

            if (!io.check_if_file_exists(
                    "/results",
                    "results_${schedule!!.date}_$lastAlertTime.csv"
                )
            ) {
                Log.d("checkalert","file not found")
                _uiState.value = _uiState.value.copy(isEvent = true)
            }
        }

        GlobalScope.launch {
            if (_uiState.value.isLoading) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }

        }


    }

    /**
     * Start the end animation for the survey.
     * The isEnd property in the _uiState is set to true and the animation is started.
     * After 2 seconds, the isEnd property is set to false and the isEvent property is set to false.
     */
    fun startEndAnimation() {
        GlobalScope.launch {

            _uiState.value = _uiState.value.copy(isEnd = true)


            delay(2000)

            _uiState.value = _uiState.value.copy(isEvent = false)
            _uiState.value = _uiState.value.copy(isEnd = false)
        }
    }

    /**
     * Enter a value for the current question in the survey.
     * The current time is obtained and used as the answer time.
     * The answer, along with the question index, schedule date, last alert time, and answer time, is written to a results file.
     * If the question index is less than the number of questions, it is incremented.
     * If the question index is equal to the number of questions, the end animation is started.
     *
     * @param questionIndex The index of the current question
     * @param answer The answer for the current question
     */
    fun enterValue(questionIndex: Int, answer: Int) {

        val answerTime = Time(System.currentTimeMillis())



        //write answer to file
        io.appendtofile(
            context = context,
            fileName = "results_${schedule!!.date}_$lastAlertTime.csv",
            path = "/results",
            data = "${schedule!!.date},$lastAlertTime,$questionIndex,$answer,${answerTime.toString()}"
        )


        //if everything worked, increase question index
        var questionIndex = _uiState.value.questionIndex

        if (questionIndex == 0 && answer == 0) {
            _uiState.value = _uiState.value.copy(questionIndex = 12)
        } else {

            if (questionIndex < StringProvider.questions.size - 1) {
                questionIndex++
                _uiState.value = _uiState.value.copy(questionIndex = questionIndex)
            } else {
                startEndAnimation()
            }
        }

    }

}