package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models

data class QuizQuestion(
    val question: String = "",
    val answer: String = "",
    val options: List<String> = listOf(),
    val category: String = "",
    val level: String = "",
    val hint: String = "",
    val explanation: String = "",
    val stage: String = "",
    val points: String = "",

)
