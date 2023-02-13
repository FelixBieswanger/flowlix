package com.example.flowlix.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.flowlix.data.schedule.Schedule
import com.example.flowlix.data.schedule.Scheduler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Time
import kotlin.math.log


class FlowViewModel(app: Application) : AndroidViewModel(app) {

    val context = getApplication<Application>().applicationContext

    private val _uiState = MutableStateFlow(FlowUIState())
    val uiState: StateFlow<FlowUIState> = _uiState.asStateFlow()

    private var schedule: Schedule
    private var lastAlertTime: Time? = null


    init {
        this.schedule = Scheduler.get_or_create_Schedule(context)
    }


    fun setNextAlarm() {
        val nextAlarm = Scheduler.getNextAlarm(this.schedule)

        Log.d("getnextAlarm", nextAlarm.toString())
        if (nextAlarm == null) {
            _uiState.value = _uiState.value.copy(nextAlarm = "No more pending")
        } else {
            _uiState.value = _uiState.value.copy(nextAlarm = nextAlarm.toString())

        }
    }

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


    fun startEndAnimation() {
        GlobalScope.launch {

            _uiState.value = _uiState.value.copy(isEnd = true)


            delay(2000)

            _uiState.value = _uiState.value.copy(isEvent = false)
            _uiState.value = _uiState.value.copy(isEnd = false)
        }
    }


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