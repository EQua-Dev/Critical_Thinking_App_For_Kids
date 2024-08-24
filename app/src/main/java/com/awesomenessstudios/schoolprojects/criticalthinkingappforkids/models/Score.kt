package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models

data class Score(
    val childId: String = "",
    val datePlayed: String = "",
    val category: String = "",
    val childStage: String = "",
    val difficulty: String = "",
    val score: Int = 0
)
