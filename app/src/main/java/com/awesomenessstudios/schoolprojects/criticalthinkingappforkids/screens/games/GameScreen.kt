package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.draganddrop.DragAndDropScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.patternpuzzle.PatternPuzzleScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.shapematcher.ShapeMatcherScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.simplemaze.SimpleMazeScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.HelpMe

enum class DifficultyLevel { EASY, MEDIUM, HARD }
enum class ChildStage { EARLY_CHILDHOOD, PRIMARY, PRE_TEEN, TEENAGER }
enum class Category { PROBLEM_SOLVING, DECISION_MAKING, LOGICAL_REASONING, CREATIVITY, MEMORY }

@Composable
fun GameScreen(childId: String, category: String, difficultyLevel: String, childStage: String) {

    val TAG = "GameScreen"


    // Convert Strings to Enums
    val categoryEnum = Category.entries.find { it.name == category.uppercase() }
    val difficultyLevelEnum =
        DifficultyLevel.entries.find { it.name == difficultyLevel.uppercase() }
    val childStageEnum =
        ChildStage.entries.find { it.name == HelpMe.convertToSnakeCase(childStage).uppercase() }

    Log.d(TAG, "GameScreen: $")
    Log.d(
            TAG,
            "GameScreen: ${categoryEnum?.name} ${childStageEnum?.name} ${difficultyLevelEnum?.name}"
        )
    // Check if the enum conversions were successful
    if (categoryEnum != null && difficultyLevelEnum != null && childStageEnum != null) {

        when (categoryEnum) {
            Category.PROBLEM_SOLVING -> {
                when (difficultyLevelEnum) {
                    DifficultyLevel.EASY -> {
                        when (childStageEnum) {
                            ChildStage.EARLY_CHILDHOOD -> DragAndDropScreen()
                            // Add cases for other stages if necessary
                            else -> DefaultScreen()
                        }
                    }

                    DifficultyLevel.MEDIUM -> {
                        when (childStageEnum) {
                            ChildStage.EARLY_CHILDHOOD -> PatternPuzzleScreen()
                            // Add cases for other stages if necessary
                            else -> DefaultScreen()
                        }
                    }

                    DifficultyLevel.HARD -> {
                        when (childStageEnum) {
                            ChildStage.EARLY_CHILDHOOD -> SimpleMazeScreen()
                            // Add cases for other stages if necessary
                            else -> DefaultScreen()
                        }
                    }
                }
            }
            // Add similar blocks for other categories if necessary
            Category.DECISION_MAKING -> {
                // Handle Decision Making category
                // Add your logic for games related to Decision Making
            }

            Category.LOGICAL_REASONING -> {
                // Handle Logical Reasoning category
                // Add your logic for games related to Logical Reasoning
            }

            Category.CREATIVITY -> {
                // Handle Creativity category
                // Add your logic for games related to Creativity
            }

            Category.MEMORY -> {
                // Handle Memory category
                // Add your logic for games related to Memory
            }
        }
    } else {
        // If the conversion failed, show a default screen
        DefaultScreen()
    }
}

@Composable
fun DefaultScreen() {
    // A default screen if no matching game is found
    Text("No game available for this category, difficulty level, and child stage.")
}
