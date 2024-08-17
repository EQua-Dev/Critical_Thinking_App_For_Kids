package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models

data class GameDetails(
    val quiz: Map<String, String> = mapOf(),
    val game: Map<String, String> = mapOf(),
    val outdoorTask: Map<String, String> = mapOf(),
    val video: Map<String, String> = mapOf(),
)
