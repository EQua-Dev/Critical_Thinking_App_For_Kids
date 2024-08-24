package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.quiz


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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
    onQuizCompleted: (score: Int, childId: String, category: String, difficulty: String, childStage: String, quizDoneReason: String) -> Unit
) {

    val quizQuestions by quizViewModel.quizQuestions.collectAsState()

    // State variables
    var score by remember { mutableStateOf(0) }
    val lives by quizViewModel.lives.collectAsState() // Start with one life
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var timeRemaining by remember { mutableIntStateOf(30) }
    val hintUsed by quizViewModel.hintUsed.collectAsState()

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
            reduceLife(
                quizViewModel,
                lives,
                onQuizCompleted,
                score,
                childId,
                categoryKey,
                difficultyLevel,
                childStage
            )
            currentQuestionIndex++
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
                        timeRemaining = 30 // Reset the timer
                        currentQuestionIndex++
                        quizViewModel.updateLives("add")
                        quizViewModel.updateHintUsed(false)
                        checkQuizEnd(
                            quizQuestions,
                            currentQuestionIndex,
                            score,
                            onQuizCompleted,
                            childId,
                            categoryKey,
                            difficultyLevel,
                            childStage
                        )
                    },
                    onIncorrectAnswer = {
                        reduceLife(
                            quizViewModel,
                            lives,
                            onQuizCompleted,
                            score,
                            childId,
                            categoryKey,
                            difficultyLevel,
                            childStage
                        )
                        currentQuestionIndex++
                        quizViewModel.updateHintUsed(false)
                    }
                )
            },
            hintUsed = hintUsed,
            onHintUsed = {
                quizViewModel.updateHintUsed(it)
            },
            answeredQuestions = currentQuestionIndex,
            totalQuestions = quizQuestions.size
        )
    }

}


@Composable
fun QuizContent(
    score: Int,
    lives: Int,
    timeRemaining: Int,
    question: String,
    hint: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    hintUsed: Boolean,
    onHintUsed: (Boolean) -> Unit,
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
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = Color(0xFF33691E),
                modifier = Modifier.padding(8.dp)
            )

            // Lives
            Row {
                repeat(5) { index ->
                    val heartColor = if (index < lives) Color.Red else Color.Gray
                    Icon(
                        imageVector = Icons.Default.Favorite, // Outline heart for lives
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
                    .background(Color.Transparent, shape = CircleShape)
            ) {
                TimerView(timeRemaining = timeRemaining)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Question Pane
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {


            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = question,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )


                    if (hint.isNotEmpty() && hintUsed) {
                        Text(
                            text = "Hint: $hint",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                if (!hintUsed) {
                    // Hint Icon
                    IconButton(
                        onClick = { onHintUsed(true) },
                        modifier = Modifier.align(Alignment.TopEnd) // Aligns the IconButton to the top right
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = "Hint",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

            }


        }

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Indicator for Questions
        Text(
            text = "Question: ${answeredQuestions + 1}/$totalQuestions",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            color = Color(0xFF33691E),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFF0F0F0),
                            shape = RoundedCornerShape(16.dp)
                        ), // Light background for options
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
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
    quizViewModel: QuizViewModel,
    lives: Int,
    onQuizCompleted: (score: Int, childId: String, category: String, difficulty: String, childStage: String, quizDoneReason: String) -> Unit,
    score: Int,
    childId: String,
    categoryKey: String,
    difficultyLevel: String,
    childStage: String,
) {
    if (lives > 1) {
        quizViewModel.updateLives("minus")
    } else {
        onQuizCompleted(
            score,
            childId,
            categoryKey,
            difficultyLevel,
            childStage,
            "You ran out of lives 💔"
        )
    }
}

fun checkQuizEnd(
    questions: List<QuizQuestion>,
    currentQuestionIndex: Int,
    score: Int,
    onQuizCompleted: (score: Int, childId: String, category: String, difficulty: String, childStage: String, quizDoneReason: String) -> Unit,
    childId: String,
    categoryKey: String,
    difficultyLevel: String,
    childStage: String
) {
    if (currentQuestionIndex >= questions.size) {
        onQuizCompleted(
            score,
            childId,
            categoryKey,
            difficultyLevel,
            childStage,
            "You have answered every available question 👏🏽"
        )
    }
}