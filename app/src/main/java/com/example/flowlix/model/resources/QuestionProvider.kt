package com.example.flowlix.model.resources

import com.example.flowlix.R
import com.example.flowlix.model.data.Activity
import com.example.flowlix.model.data.Question


/**
 * QuestionProvider object containing questions with answerposibilites.
 * Made so its interchangeable.
 *
 * @property questions A list of strings representing the questions used in the app.
 * @property activities A list of [Activity] objects representing the activities used in the app.
 */
object QuestionProvider {

    val questions = listOf(
        Question(
            question = "Do you experience Flow right now?",
            answerpossibilites = listOf("Yes","No")
        ),
        Question(
            question = "Rate the overall intensity of your flow experience",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "I feel just the right amount of challenge",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "My thoughts/activites run fluidly and smoothly",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "I don't notice time passing",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "I have not difficulty concentrating",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "My mind is completely clear",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "I am totally absorbed in what I am doing",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "The right thoughts/movements occur on their own accord",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "I know what I have to do each step of the way",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "I feel that I have everything under control",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "I am completely lost in thought",
            answerpossibilites =  (1..7).toList()
        ),
        Question(
            question = "Select Current Activity",
            answerpossibilites =  listOf(
                Activity("Working",R.drawable.suitcase),
                Activity("Chores",R.drawable.chores),
                Activity("Exercise",R.drawable.excerise),
                Activity("Walking",R.drawable.walk),
                Activity("Other",R.drawable.other)
            )
        ),
    )
}