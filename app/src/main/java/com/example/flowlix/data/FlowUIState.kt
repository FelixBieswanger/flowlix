package com.example.flowlix.data

data class FlowUIState (
    val isEnd: Boolean = false,
    val isEvent: Boolean = false,
    val isLoading: Boolean = true,
    val questionIndex: Int = 0,
    val nextAlarm: String = ""
)