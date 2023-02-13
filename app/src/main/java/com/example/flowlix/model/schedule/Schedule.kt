package com.example.flowlix.model.schedule

/**
Schedule class holds information about a schedule for a specific date.
@property date the date of the schedule
@property alerts the list of alerts in the schedule
 */
data class Schedule (
    val date: String,
    val alerts: List<String>
)