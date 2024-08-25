package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.sortinggame

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.ShapeItem
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.ShapeType
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.PentagonShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.TriangleShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.SortingGameViewModel
import kotlin.math.roundToInt

@Composable
fun SortingGameScreen(viewModel: SortingGameViewModel = hiltViewModel()) {
    val gameState by viewModel.gameState.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Display the grey shapes
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            gameState?.shapes?.filter { it.color == Color.Gray }?.forEach { shape ->
                ShapeView(shape)
            }
        }

        // Display the colored shapes
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            gameState?.shapes?.filter { it.color != Color.Gray }?.forEach { shape ->
                DraggableShapeView(shape, onDragEnd = { targetShape ->
                    viewModel.onShapeDropped(targetShape, shape)
                })
            }
        }

        // Check button
        Button(
            onClick = { viewModel.checkMatches() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Check Matches")
        }

        // Display error message if any
        gameState?.errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun ShapeView(shape: ShapeItem) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(shape.color, shape.toShape())
    )
}

@Composable
fun DraggableShapeView(shape: ShapeItem, onDragEnd: (ShapeItem) -> Unit) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    val TAG = "DraggableShapeView"

    Box(
        modifier = Modifier
            .size(100.dp)
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .background(shape.color, shape.toShape())
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    Log.d(TAG, "DraggableShapeView: $shape")
                    onDragEnd(shape)
                })
            }
    )
}

fun ShapeItem.toShape(): Shape {
    return when (this.type) {
        ShapeType.CIRCLE -> CircleShape
        ShapeType.SQUARE -> RoundedCornerShape(0.dp)
        ShapeType.TRIANGLE -> TriangleShape
        ShapeType.OVAL -> RoundedCornerShape(50.dp)
        ShapeType.RECTANGLE -> RoundedCornerShape(4.dp)
        ShapeType.PENTAGON -> PentagonShape
    }
}



// Custom shapes such as TriangleShape and PentagonShape would be implemented separately.

