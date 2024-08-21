package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.draganddrop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.R

data class Shape(val shape: Int, val color: Color)

@Composable
fun DragAndDropScreen() {
    val shapes = listOf(
        Shape(R.drawable.circle, Color.Red),
        Shape(R.drawable.square, Color.Blue),
        // Add more shapes here
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Display the target shape
        TargetShape(shape = shapes.random())

        // Display the options to match
        OptionsRow(shapes = shapes)
    }
}

@Composable
fun TargetShape(shape: Shape) {
    Image(painter = painterResource(id = shape.shape), contentDescription = "Target Shape", modifier = Modifier
        .size(200.dp)
        .padding(16.dp))
}

@Composable
fun OptionsRow(shapes: List<Shape>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (shape in shapes) {
            OptionShape(shape = shape)
        }
    }
}

@Composable
fun OptionShape(shape: Shape) {
    Box(modifier = Modifier
        .size(150.dp)
        .padding(8.dp)
        .clickable {
            // Handle shape selection logic here
        }) {
        Image(painter = painterResource(id = shape.shape), contentDescription = "Option Shape")
    }
}