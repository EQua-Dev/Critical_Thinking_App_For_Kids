package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models

data class LeaderboardEntry(
    val childId: String,
    val childName: String,
    val score: Int,
    val datePlayed: String,
    val category: String,
    val difficulty: String
)