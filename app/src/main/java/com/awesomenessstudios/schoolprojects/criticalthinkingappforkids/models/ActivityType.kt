package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models

sealed class ActivityType(
    val title: String,
    val description: String,
    val activityTypeKey: String
) {
    object Quiz : ActivityType(
        title = "Quiz",
        description = "Test your knowledge with a series of challenging questions.",
        activityTypeKey = "quiz"
    )

    object Game : ActivityType(
        title = "Game",
        description = "Enjoy fun and educational games that sharpen your skills.",
        activityTypeKey = "game"
    )

    object OutdoorTask : ActivityType(
        title = "Outdoor Task",
        description = "Complete exciting tasks outside to learn through play.",
        activityTypeKey = "outdoor_task"
    )

    object Video : ActivityType(
        title = "Video",
        description = "Watch engaging videos that explain concepts in a fun way.",
        activityTypeKey = "video"
    )
}
fun getActivityTypeByKey(activityTypeKey: String): ActivityType? {
    return when (activityTypeKey) {
        ActivityType.Quiz.activityTypeKey -> ActivityType.Quiz
        ActivityType.Game.activityTypeKey -> ActivityType.Game
        ActivityType.OutdoorTask.activityTypeKey -> ActivityType.OutdoorTask
        ActivityType.Video.activityTypeKey -> ActivityType.Video
        else -> null
    }
}
