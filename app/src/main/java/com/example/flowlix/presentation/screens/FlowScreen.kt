package com.example.flowlix.presentation


import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.example.flowlix.R
import com.example.flowlix.model.data.Activity
import com.example.flowlix.viewmodel.FlowViewModel
import com.example.flowlix.model.resources.QuestionProvider
import com.example.flowlix.presentation.screens.NoActivityScreen
import java.util.*


/**
 * This code defines a FlowScreen composable function which is used to show a screen for an activity
 * tracking app.
 *
 * The function takes a flowViewModel parameter which is an instance of the [FlowViewModel]
 * class that provides the data and state of the app. The function uses the Column composable to arrange
 * its children in a vertical manner, with the Modifier used to set the height, fill the maximum
 * width and height of the screen.
 *
 * The [flowUIState] is obtained by using the collectAsState function on the uiState property of the
 * [flowViewModel]. This is done to keep the value of [flowUIState] up-to-date whenever the
 * uiState of the [flowViewModel] changes. A check animation is defined using AnimatedImageVector and
 * rememberAnimatedVectorPainter. The animation is then used in an Image composable.
 *
 * The rest of the code displays the question, and options for the user to select from.
 * The options are implemented as buttons and the selected option is entered into the [flowViewModel]
 * by calling the enterValue method.
 */

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun FlowScreen(modifier: Modifier = Modifier, flowViewModel: FlowViewModel) {
    val flowUIState = flowViewModel.uiState.collectAsState()
    flowViewModel.checkifalert()
    flowViewModel.setNextAlarm()

    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(id = R.drawable.check_anim),
        atEnd = flowUIState.value.isEnd)


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        if (flowUIState.value.isLoading == false) {
            if (flowUIState.value.isEvent) {
                if (flowUIState.value.isEnd) {

                    Box(){
                        Image(
                            modifier = Modifier
                                .size(110.dp)
                                .align(Alignment.Center),
                            painter = painter,
                            contentDescription = ""
                        )
                    }
                } else {


                    val questionIndex = flowUIState.value.questionIndex
                    val question = QuestionProvider.questions[questionIndex]

                    if (questionIndex < QuestionProvider.questions.size - 1) {

                        Text(
                            text = question.question,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(30.dp, 10.dp)
                                .height(60.dp)
                        )

                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .fillMaxWidth()
                                .padding(20.dp, 10.dp)
                                .height(60.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (question.answerpossibilites.size == 2) {

                                Button(
                                    modifier = Modifier
                                        .padding(5.dp, 0.dp)
                                        .size(60.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            0xFF43968A
                                        )
                                    ),
                                    onClick = { flowViewModel.enterValue(questionIndex, 1) }) {
                                    Text(text= question.answerpossibilites[0].toString())
                                }
                                Button(
                                    modifier = Modifier
                                        .padding(5.dp, 0.dp)
                                        .size(60.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            0xFF43968A
                                        )
                                    ),
                                    onClick = { flowViewModel.enterValue(questionIndex, 0) }) {
                                    Text(text = question.answerpossibilites[1].toString())
                                }

                            } else if (question.answerpossibilites.size > 2){
                                Spacer(modifier = Modifier.weight(1f))

                                question.answerpossibilites.forEachIndexed { index, answer ->
                                    Button(
                                        modifier = Modifier
                                            .padding(5.dp, 0.dp)
                                            .size(60.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(
                                                0xFF43968A
                                            )
                                        ),
                                        onClick = {
                                            flowViewModel.enterValue(
                                                questionIndex,
                                                answer = index+1
                                            )
                                        }
                                    ) {
                                        Text(text = answer.toString())
                                    }
                                }

                            }
                        }
                    } else {


                        val activities: List<Activity> = question.answerpossibilites as List<Activity>
                        print(activities.toString())

                        Text(
                            text = question.question,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(20.dp, 20.dp)
                                .height(15.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(30.dp,10.dp),
                        ) {
                            for ((index, activity) in activities.withIndex()) {
                                if (index % 2 == 0) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        for (i in index until index + 2) {
                                            if (i < activities.size) {
                                                val random = (-100..100).random()
                                                Button(
                                                    modifier = Modifier
                                                        .size(75.dp)
                                                        .padding(5.dp,5.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        backgroundColor = Color(
                                                            0xFF43968A
                                                        )
                                                    ),
                                                    onClick = {

                                                        flowViewModel.enterValue(questionIndex,i)

                                                    }) {
                                                    Icon(
                                                        painter = painterResource(id = activities[i].iconPath),
                                                        tint = Color(255, 255, 255),
                                                        contentDescription = activities[i].name,
                                                        modifier = Modifier.size(30.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            } else {
                NoActivityScreen(flowUIState = flowUIState)
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }
    }
}