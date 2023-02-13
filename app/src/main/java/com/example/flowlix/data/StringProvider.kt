package com.example.flowlix.data

import com.example.flowlix.R

object StringProvider {
    val questions = listOf<String>(
        "Do you experience Flow right now?",
        "Rate the overall intensity of your flow experience",
        "I feel just the right amount of challenge",
        "My thoughts/activites run fluidly and smoothly",
        "I don't notice time passing",
        "I have not difficulty concentrating",
        "My mind is completely clear",
        "I am totally absorbed in what I am doing",
        "The right thoughts/movements occur on their own accord",
        "I know what I have to do each step of the way",
        "I feel that I have everything under control",
        "I am completely lost in thought",
        "Select Current Activity",
    )


    val activities = listOf<Activity>(
        Activity("Working",R.drawable.suitcase),
        Activity("Chores",R.drawable.chores),
        Activity("Exercise",R.drawable.excerise),
        Activity("Walking",R.drawable.walk),
        Activity("Other",R.drawable.other)
    )
}