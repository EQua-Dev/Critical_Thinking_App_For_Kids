package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.simplemaze


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.random.Random

@Composable
fun SimpleMazeScreen() {
    val mazeSize = 5
    val playerPosition = remember { mutableStateOf(Offset(0f, 0f)) }
    val goalPosition = remember { mutableStateOf(Offset((mazeSize - 1) * 50f, (mazeSize - 1) * 50f)) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Navigate the maze to the goal", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Maze layout
        Box(modifier = Modifier.size((mazeSize * 50).dp)) {
            // Goal
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Red)
                    .offset { IntOffset(goalPosition.value.x.toInt(), goalPosition.value.y.toInt()) }
            )

            // Player
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Blue)
                    .offset { IntOffset(playerPosition.value.x.toInt(), playerPosition.value.y.toInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consumeAllChanges()
                            val newPosition = Offset(
                                (playerPosition.value.x + dragAmount.x).coerceIn(0f, (mazeSize - 1) * 50f),
                                (playerPosition.value.y + dragAmount.y).coerceIn(0f, (mazeSize - 1) * 50f)
                            )
                            playerPosition.value = newPosition
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (playerPosition.value == goalPosition.value) {
            Text("Congratulations! You've reached the goal!", color = Color.Green, style = MaterialTheme.typography.bodyLarge)
        }
    }
}