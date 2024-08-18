package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.patternpuzzle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun PatternPuzzleScreen(modifier: Modifier = Modifier) {
    val shapes = listOf("Circle", "Square", "Triangle")
    val sequence = remember { mutableStateListOf(shapes[Random.nextInt(shapes.size)]) }
    var userSequence by remember { mutableStateOf(mutableListOf<String>()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Complete the pattern:", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Display the pattern
        Row {
            sequence.forEach {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(getColorForShape(it))
                        .border(2.dp, Color.Black)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User interaction buttons
        Row {
            shapes.forEach { shape ->
                Button(
                    onClick = {
                        userSequence.add(shape)
                        if (userSequence.size == sequence.size) {
                            if (userSequence == sequence) {
                                // User has completed the sequence
                                sequence.add(shapes[Random.nextInt(shapes.size)])
                                userSequence.clear()
                            } else {
                                // Incorrect sequence
                                userSequence.clear()
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp)
                        .background(getColorForShape(shape))
                ) {
                    BasicText(shape)
                }
            }
        }
    }
}

@Composable
fun getColorForShape(shape: String): Color {
    return when (shape) {
        "Circle" -> Color.Red
        "Square" -> Color.Green
        "Triangle" -> Color.Blue
        else -> Color.Gray
    }
}