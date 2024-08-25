package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.shapesandcolors

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Path
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.ui.theme.Typography
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.HexagonShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.LoadingDialog
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.OctagonShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.PentagonShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.RhombusShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.StarShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.TrapeziumShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.TriangleShape

@Composable
fun ShapesAndColorsScreen() {
    /* Card(
         modifier = Modifier.padding(16.dp),
     ) {*/
    val shapesList = mutableListOf<String>()
    var finalShapesList by remember {
        mutableStateOf(listOf<String>())
    }
    var displayShapesList by remember {
        mutableStateOf(listOf<String>())
    }

    val colorsList: MutableList<Pair<Color, String>> = mutableListOf()
    var finalColorsList: List<Pair<Color, String>> by remember {
        mutableStateOf(listOf())
    }
    var displayColorsList: List<Pair<Color, String>> by remember {
        mutableStateOf(listOf())
    }

    var displayGroupList: MutableList<Pair<Pair<Color, String>, String>> =
        mutableListOf()

    var finalDisplayGroupList: List<Pair<Pair<Color, String>, String>> by remember {
        mutableStateOf(listOf())
    }

    val userScore = remember {
        mutableIntStateOf(0)
    }

    val displayColor = remember {
        mutableStateOf(Pair(Color.Red, "Red"))
    }

    val displayShape = remember {
        mutableStateOf("")
    }

    val isDoneAddingShapes = remember {
        mutableStateOf(false)
    }

    val isDoneAddingColors = remember {
        mutableStateOf(false)
    }
    val shouldRefresh = remember {
        mutableStateOf(false)
    }
    val shouldRelaunch = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(shouldRefresh.value) {
        shouldRefresh.value = false
        Log.d("TAG", "GameScreen: isrefreshing")
        for (shapes in ShapeType.entries) {
            Log.d("TAG", "GameScreen1: $isDoneAddingShapes")
            shapesList.add(shapes.shape)
        }
        if (shapesList.size == ShapeType.entries.size) {
            isDoneAddingShapes.value = true
            shapesList.shuffle()
            Log.d("TAG", "GameScreen: $shapesList")
        }
        for (colors in ColorType.entries) {
            colorsList.add(Pair(colors.color, colors.colorName))
        }
        if (colorsList.size == ColorType.entries.size) {
            isDoneAddingColors.value = true
            Log.d("TAG", "GameScreen, colors: $colorsList")
        }
        finalShapesList = shapesList.toList().shuffled()
        displayShapesList = finalShapesList.take(4)
        finalColorsList = colorsList.toList().shuffled()
        displayColorsList = finalColorsList.take(4)

        for (shape in displayShapesList) {
            var pairToAdd = Pair(Pair(Color.Red, "Red"), "Triangle")
            //for (color in displayColorsList){
            pairToAdd = Pair(displayColorsList.random(), shape)
            //break
            //}
            displayGroupList.add(pairToAdd)
            Log.d("TAG", "GameScreen, displayGroupList: $displayGroupList")
        }
        finalDisplayGroupList = displayGroupList.toList()

    }

    if (isDoneAddingShapes.value && isDoneAddingColors.value) {
        val randomNumber = (0..3).random()
        displayShape.value = finalDisplayGroupList[randomNumber].second//displayShapesList.random()
        displayColor.value = finalDisplayGroupList[randomNumber].first//displayColorsList.random()

        Box(modifier = Modifier.padding(32.dp), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Score: ${userScore.intValue}", style = Typography.displayMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text("Select the ", style = Typography.titleMedium)
                    Text(
                        displayColor.value.second,
                        fontWeight = FontWeight.Bold,
                        style = Typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        displayShape.value,
                        fontWeight = FontWeight.Bold,
                        style = Typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Triangle
                    ShapeCard(
                        cardShape = finalDisplayGroupList[0].second,
                        cardColor = finalDisplayGroupList[0].first
                    ) { color, shape ->
                        if (shape == displayShape.value && color == displayColor.value.second) {
                            userScore.intValue++
                            shouldRefresh.value = true
                        }

                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Circle
                    ShapeCard(
                        cardShape = finalDisplayGroupList[1].second,
                        cardColor = finalDisplayGroupList[1].first
                    ) { color, shape ->
                        if (shape == displayShape.value && color == displayColor.value.second) {
                            userScore.intValue++
                            shouldRefresh.value = true
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Rectangle
                    ShapeCard(
                        cardShape = finalDisplayGroupList[2].second,
                        cardColor = finalDisplayGroupList[2].first
                    ) { color, shape ->
                        if (shape == displayShape.value && color == displayColor.value.second) {
                            userScore.intValue++
                            shouldRefresh.value = true
                        }

                    }

                    // Oval
                    ShapeCard(
                        cardShape = finalDisplayGroupList[3].second,
                        cardColor = finalDisplayGroupList[3].first
                    ) { color, shape ->
                        if (shape == displayShape.value && color == displayColor.value.second) {
                            userScore.intValue++
                            shouldRefresh.value = true
                        }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        userScore.intValue = 0
                        shouldRefresh.value = true
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Reset")
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingDialog()
        }
    }

}

@Composable
fun ShapeCard(
    cardShape: String,
    cardColor: Pair<Color, String>,
    onClick: (color: String, shape: String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick(cardColor.second, cardShape)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        DrawShape(shapeType = cardShape, shapeColor = cardColor.first)
    }
}

@Composable
fun DrawShape(shapeType: String, shapeColor: Color) {
    val shapeSize = 120.dp

    Canvas(
        modifier = Modifier
            .size(shapeSize)
            .padding(12.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        when (shapeType) {
            ShapeType.TRIANGLE.shape -> {
                val trianglePath = Path().apply {
                    moveTo(canvasWidth / 2f, 0f) // Top point
                    lineTo(0f, canvasHeight) // Bottom-left point
                    lineTo(canvasWidth, canvasHeight) // Bottom-right point
                    close() // Close the path
                }
                drawPath(
                    path = trianglePath,
                    color = shapeColor,
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            ShapeType.CIRCLE.shape -> {
                drawCircle(
                    color = shapeColor,
                    radius = canvasWidth / 2f,
                    center = Offset(canvasWidth / 2, canvasHeight / 2),
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            ShapeType.RECTANGLE.shape -> {
                drawRect(
                    color = shapeColor,
                    size = Size(canvasWidth, canvasHeight),
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            ShapeType.SQUARE.shape -> {
                drawRect(
                    color = shapeColor,
                    size = Size(canvasWidth, canvasHeight),
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            ShapeType.TRAPEZIUM.shape -> {
                val trapeziumPath = Path().apply {
                    moveTo(0f, canvasHeight) // Bottom-left point
                    lineTo(canvasWidth * 0.3f, 0f) // Top-left point
                    lineTo(canvasWidth * 0.7f, 0f) // Top-right point
                    lineTo(canvasWidth, canvasHeight) // Bottom-right point
                    close() // Close the path
                }
                drawPath(
                    path = trapeziumPath,
                    color = shapeColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            ShapeType.RHOMBUS.shape -> {
                val rhombusPath = Path().apply {
                    moveTo(canvasWidth / 2f, 0f) // Top point
                    lineTo(0f, canvasHeight / 2f) // Left point
                    lineTo(canvasWidth / 2f, canvasHeight) // Bottom point
                    lineTo(canvasWidth, canvasHeight / 2f) // Right point
                    close() // Close the path
                }
                drawPath(
                    path = rhombusPath,
                    color = shapeColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            ShapeType.PENTAGON.shape -> {
                val pentagonPath = Path().apply {
                    moveTo(canvasWidth / 2f, 0f) // Top point
                    lineTo(0f, canvasHeight * 0.4f) // Top-left point
                    lineTo(canvasWidth * 0.4f, canvasHeight) // Bottom-left point
                    lineTo(canvasWidth * 0.6f, canvasHeight) // Bottom-right point
                    lineTo(canvasWidth, canvasHeight * 0.4f) // Top-right point
                    close() // Close the path
                }
                drawPath(
                    path = pentagonPath,
                    color = shapeColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            ShapeType.HEXAGON.shape -> {
                val hexagonPath = Path().apply {
                    moveTo(canvasWidth / 2f, 0f) // Top point
                    lineTo(canvasWidth * 0.25f, canvasHeight * 0.5f) // Top-left point
                    lineTo(canvasWidth * 0.75f, canvasHeight * 0.5f) // Top-right point
                    lineTo(canvasWidth, canvasHeight.toFloat()) // Bottom-right point
                    lineTo(canvasWidth * 0.75f, canvasHeight.toFloat()) // Bottom-left point
                    lineTo(canvasWidth * 0.25f, canvasHeight * 0.5f) // Bottom-left point
                    close() // Close the path
                }
                drawPath(
                    path = hexagonPath,
                    color = shapeColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            ShapeType.OCTAGON.shape -> {
                val octagonPath = Path().apply {
                    moveTo(canvasWidth * 0.3f, 0f) // Top-left point
                    lineTo(canvasWidth * 0.7f, 0f) // Top-right point
                    lineTo(canvasWidth.toFloat(), canvasHeight * 0.3f) // Top-right corner point
                    lineTo(
                        canvasWidth.toFloat(),
                        canvasHeight * 0.7f
                    ) // Bottom-right corner point
                    lineTo(canvasWidth * 0.7f, canvasHeight.toFloat()) // Bottom-right point
                    lineTo(canvasWidth * 0.3f, canvasHeight.toFloat()) // Bottom-left point
                    lineTo(0f, canvasHeight * 0.7f) // Bottom-left corner point
                    lineTo(0f, canvasHeight * 0.3f) // Top-left corner point
                    close() // Close the path
                }
                drawPath(
                    path = octagonPath,
                    color = shapeColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            ShapeType.STAR.shape -> {
                val starPath = Path().apply {
                    val innerRadius = canvasWidth / 4
                    val outerRadius = canvasWidth / 2

                    for (i in 0 until 10) {
                        val angle = Math.PI / 5 * i
                        val radius = if (i % 2 == 0) innerRadius else outerRadius
                        val x = canvasWidth / 2 + radius * Math.cos(angle).toFloat()
                        val y = canvasHeight / 2 + radius * Math.sin(angle).toFloat()

                        if (i == 0) {
                            moveTo(x, y)
                        } else {
                            lineTo(x, y)
                        }
                    }
                    close() // Close the path
                }
                drawPath(
                    path = starPath,
                    color = shapeColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }
        }
    }
}

enum class ShapeType(val shape: String) {
    TRIANGLE("Triangle"),
    CIRCLE("Circle"),
    RECTANGLE("Rectangle"),
    SQUARE("Square"),
    TRAPEZIUM("Trapezium"),
    RHOMBUS("Rhombus"),
    PENTAGON("Pentagon"),
    HEXAGON("Hexagon"),
    OCTAGON("Octagon"),
    STAR("Star")
}

enum class ColorType(val color: Color, val colorName: String) {
    RED(Color.Red, "Red"),
    BLUE(Color.Blue, "Blue"),
    GREEN(Color.Green, "Green"),
    YELLOW(Color.Yellow, "Yellow"),
}
