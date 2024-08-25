package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

enum class ShapeType {
    CIRCLE, SQUARE, TRIANGLE, OVAL, RECTANGLE, PENTAGON
}

data class ShapeItem(
    val type: ShapeType,
    val color: Color,
    val isMatched: Boolean = false,
    val position: Offset = Offset.Zero // Add this property
)


data class GameState(
    val shapes: List<ShapeItem>,
    val correctMatches: Int = 0,
    val errorMessage: String = ""
)
