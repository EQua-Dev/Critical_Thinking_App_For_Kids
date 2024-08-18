package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.QuizQuestion
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.QuizViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    childId: String,
    categoryKey: String,
    childStage: String,
    difficultyLevel: String,
    quizViewModel: QuizViewModel = hiltViewModel(),
    onQuizCompleted: (score: Int) -> Unit
) {

    val quizQuestions by quizViewModel.quizQuestions.collectAsState()

    // State variables
    var score by remember { mutableStateOf(0) }
    var lives = remember { mutableStateOf(1) } // Start with one life
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var timeRemaining by remember { mutableStateOf(30) }
    var hintUsed by remember { mutableStateOf(false) }

    // Get the current question
    val currentQuestion = quizQuestions.getOrNull(currentQuestionIndex)

    LaunchedEffect(Unit) {
        quizViewModel.fetchQuizQuestions(categoryKey, difficultyLevel, childStage)
    }
    // Timer logic
    LaunchedEffect(timeRemaining, currentQuestionIndex) {
        if (timeRemaining > 0) {
            delay(1000L)
            timeRemaining -= 1
        } else {
            // When time runs out
            reduceLife(lives, onQuizCompleted, score)
        }
    }




    if (currentQuestion != null) {
        QuizContent(
            score = score,
            lives = lives,
            timeRemaining = timeRemaining,
            question = currentQuestion.question,
            hint = currentQuestion.hint,
            options = currentQuestion.options,
            onOptionSelected = { selectedOption ->
                handleOptionSelection(
                    selectedOption,
                    currentQuestion.answer,
                    onCorrectAnswer = {
                        score += if (hintUsed) currentQuestion.points.toInt() - 2 else currentQuestion.points.toInt()
                        hintUsed = false
                        timeRemaining = 30 // Reset the timer
                        currentQuestionIndex++
                        checkQuizEnd(quizQuestions, currentQuestionIndex, score, onQuizCompleted)
                    },
                    onIncorrectAnswer = {
                        reduceLife(lives, onQuizCompleted, score)
                    }
                )
            },
            onHintUsed = {
                hintUsed = true
            },
            answeredQuestions = currentQuestionIndex,
            totalQuestions = quizQuestions.size
        )
    }

}


@Composable
fun QuizContent(
    score: Int,
    lives: MutableState<Int>,
    timeRemaining: Int,
    question: String,
    hint: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    onHintUsed: () -> Unit,
    answeredQuestions: Int,
    totalQuestions: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Row: Score, Lives, and Timer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Score
            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )

            // Lives
            Row {
                repeat(5) { index ->
                    val heartColor = if (index < lives.value) Color.Red else Color.Gray
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = heartColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Timer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.LightGray, shape = CircleShape)
            ) {
                TimerView(timeRemaining = timeRemaining)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Question Pane
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )

                if (hint.isNotEmpty()) {
                    Text(
                        text = "Hint: $hint",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Hint Icon
            IconButton(
                onClick = onHintUsed,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Help, contentDescription = "Hint", tint = Color.Blue)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Indicator for Questions
        Text(
            text = "Question: ${answeredQuestions + 1}/$totalQuestions",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            options.forEach { option ->
                Button(
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = option)
                }
            }
        }
    }
}

@Composable
fun TimerView(timeRemaining: Int) {
    val circleColor = Color.Red
    Canvas(
        modifier = Modifier.size(40.dp),
        onDraw = {
            drawArc(
                color = circleColor,
                startAngle = -90f,
                sweepAngle = (timeRemaining.toFloat() / 30f) * 360f,
                useCenter = false,
                style = Stroke(4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    )
    Text(
        text = "$timeRemaining",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
    )
}

fun handleOptionSelection(
    selectedOption: String,
    correctAnswer: String,
    onCorrectAnswer: () -> Unit,
    onIncorrectAnswer: () -> Unit
) {
    if (selectedOption == correctAnswer) {
        onCorrectAnswer()
    } else {
        onIncorrectAnswer()
    }
}

fun reduceLife(
    lives: MutableState<Int>,
    onQuizCompleted: (score: Int) -> Unit,
    score: Int,
) {
    if (lives.value > 1) {
        lives.value -= 1
    } else {
        onQuizCompleted(score)
    }
}

fun checkQuizEnd(
    questions: List<QuizQuestion>,
    currentQuestionIndex: Int,
    score: Int,
    onQuizCompleted: (score: Int) -> Unit
) {
    if (currentQuestionIndex >= questions.size) {
        onQuizCompleted(score)
    }
}