package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.GameState
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.ShapeItem
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.ShapeType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.random.Random

@HiltViewModel
class SortingGameViewModel @Inject constructor() : ViewModel() {
    private val allShapes = listOf(
        ShapeType.CIRCLE, ShapeType.SQUARE, ShapeType.TRIANGLE,
        ShapeType.OVAL, ShapeType.RECTANGLE, ShapeType.PENTAGON
    )

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState

    init {
        resetGame()
    }

    fun resetGame() {
        val selectedShapes = allShapes.shuffled().take(3)
        val shapes = selectedShapes.flatMap { shape ->
            listOf(
                ShapeItem(type = shape, color = Color.Gray),
                ShapeItem(type = shape, color = randomColorForShape(shape))
            )
        }.shuffled()
        _gameState.value = GameState(shapes = shapes)
    }

    fun onShapeDropped(targetShape: ShapeItem, draggedShape: ShapeItem) {
        val currentShapes = _gameState.value?.shapes?.toMutableList() ?: return

        if (targetShape.type == draggedShape.type && targetShape.color == Color.Gray) {
            val index = currentShapes.indexOf(targetShape)
            currentShapes[index] = targetShape.copy(isMatched = true)
            _gameState.value = _gameState.value?.copy(
                shapes = currentShapes,
                correctMatches = _gameState.value?.correctMatches?.plus(1) ?: 0
            )

            if (currentShapes.all { it.isMatched }) {
                // All shapes matched, reset the game
                resetGame()
            }
        }
    }

    fun checkMatches() {
        val shapes = _gameState.value?.shapes ?: return
        if (shapes.all { it.isMatched }) {
            // All shapes are matched
            resetGame()
        } else {
            // Not all shapes are matched
            _gameState.value = _gameState.value?.copy(
                errorMessage = "Shapes do not match! Try again."
            )
        }
    }

    private fun randomColorForShape(shape: ShapeType): Color {
        return when (shape) {
            ShapeType.CIRCLE -> Color.Red
            ShapeType.SQUARE -> Color.Green
            ShapeType.TRIANGLE -> Color.Blue
            ShapeType.OVAL -> Color.Yellow
            ShapeType.RECTANGLE -> Color.Cyan
            ShapeType.PENTAGON -> Color.Magenta
        }
    }
}

