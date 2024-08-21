package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.shapematcher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun ShapeMatcherScreen() {
    val shapes = listOf("Circle", "Square", "Triangle")
    val shapeColors = listOf(Color.Red, Color.Green, Color.Blue)
    var draggedCustomShape by remember { mutableStateOf<CustomShape?>(null) }
    var targetCustomShape by remember { mutableStateOf(CustomShape("", Offset(0f, 0f))) }
    var matchedCustomShape by remember { mutableStateOf<CustomShape?>(null) }

    // Randomly assign a shape and position for matching
    LaunchedEffect(Unit) {
        targetCustomShape = CustomShape(shapes.random(), Offset(Random.nextFloat() * 300f, Random.nextFloat() * 300f))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.LightGray) // Add a background color for better contrast
    ) {
        Text("Match the shape!", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Get the color index safely
        val colorIndex = shapes.indexOf(targetCustomShape.type)
        val targetColor = if (colorIndex != -1) shapeColors[colorIndex] else Color.Gray

        // Display the target shape
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(targetColor, CircleShape)
                .offset { IntOffset(targetCustomShape.position.x.toInt(), targetCustomShape.position.y.toInt()) }
        )


        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            shapes.forEach { shape ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White, getShape(shape))
                        //.border(2.dp, Color.Black, shape) // Add a border for clarity
                        .pointerInput(Unit) {
                            // ... (pointer input handling remains the same)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (draggedCustomShape != null) {
            val isMatch = draggedCustomShape?.type == targetCustomShape.type
            if (isMatch) {
                matchedCustomShape = draggedCustomShape
                Text("Matched!", color = Color.Green, style = MaterialTheme.typography.bodyLarge)
            } else {
                Text("Try Again!", color = Color.Red, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}


@Composable
fun getShape(shapeType: String): Shape {
    return when (shapeType) {
        "Circle" -> CircleShape
        "Square" -> RoundedCornerShape(0.dp)
        "Triangle" -> TriangleShape // You may need to implement TriangleShape or use an alternative
        else -> CircleShape
    }
}

data class CustomShape(val type: String, val position: Offset)

object TriangleShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}