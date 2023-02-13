package com.example.flowlix.model.data

data class Question<T> (
    val question: String,
    val answerpossibilites: List<T>
    )
