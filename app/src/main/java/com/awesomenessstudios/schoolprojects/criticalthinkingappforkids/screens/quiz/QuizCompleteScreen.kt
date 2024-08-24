package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.R
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.LottieAnimationView
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Score
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation.Screen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.HelpMe.revertFromSnakeCase
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.QuizViewModel

@Composable
fun QuizCompleteScreen(
    score: Int,
    childId: String,
    category: String,
    difficulty: String,
    childStage: String,
    quizDoneReason: String,
    viewModel: QuizViewModel = hiltViewModel(),
    onComplete: (String, Boolean) -> Unit
) {
    val scores by viewModel.scores.observeAsState(emptyList())
    val isHighScore = viewModel.isHighScore(score, scores)
    var confettiVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchScores(childId, category, childStage, difficulty)
        if (isHighScore) {
            confettiVisible = true
        }
    }

    Box(
        modifier = Modifier
            .background(Color.Transparent) // Ensure the box is transparent
    ) {

        if (isHighScore) {
            // Lottie Animation in the background
            LottieAnimationView(
                lottieFile = R.raw.confetti,
                modifier = Modifier
                    .fillMaxSize() // Make the animation fill the box
                    .align(Alignment.Center) // Center the animation
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Congratulatory Text
            LottieAnimationView(
                lottieFile = R.raw.finish,
                modifier = Modifier.size(150.dp)
            )
            
            Text(
                text = "Quiz Complete!",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                color = Color(0xFF33691E), // Vibrant green color
            )

            // Score Display with High Score Indicator
            Text(
                text = "You scored $score!",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                color = Color(0xFF444444), // Grayish text color
            )
            if (isHighScore) {

                Text(
                    text = "New High Score!",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    color = Color(0xFFF59E0B), // Orange color for high score
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)) // Light background color
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Category: ${revertFromSnakeCase(category)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF444444), // Grayish text color
                    )
                    Text(
                        text = "Difficulty: $difficulty",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF444444), // Grayish text color
                    )
                    Text(
                        text = "Stage: $childStage",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF444444), // Grayish text color
                    )
                    Text(
                        text = "Reason: $quizDoneReason",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF444444), // Grayish text color
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveScoreToFirestore(
                        Score(
                            childId = childId,
                            datePlayed = System.currentTimeMillis()
                                .toString(),  // Implement getCurrentDate() to get the current date
                            category = category,
                            childStage = childStage,
                            difficulty = difficulty,
                            score = score
                        )
                    )
                    onComplete(Screen.ParentHome.route, true)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text("Okay", color = Color.White)
            }

//        if (confettiVisible) {
//            ConfettiAnimation()  // Implement ConfettiAnimation() to show confetti
//        }
        }

    }


}

@Composable
fun ConfettiAnimation() {
    // Implement your confetti animation here
}
