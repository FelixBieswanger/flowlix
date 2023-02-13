package com.example.flowlix.data


/**
Data class to store the UI state in the [FlowViewModel].
@property isEnd Indicates whether the end animation is running or not.
@property isEvent Indicates whether there is an event to respond to or not.
@property isLoading Indicates whether the loading animation is running or not.
@property questionIndex The index of the question that should be displayed.
@property nextAlarm The string representation of the time of the next alarm.
 */
data class FlowUIState (
    val isEnd: Boolean = false,
    val isEvent: Boolean = false,
    val isLoading: Boolean = true,
    val questionIndex: Int = 0,
    val nextAlarm: String = ""
)