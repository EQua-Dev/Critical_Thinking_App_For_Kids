package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.R
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.LottieAnimationView
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.getActivityTypeByKey
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.getCategoryByKey
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation.Screen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components.CustomTopAppBar
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.HelpMe
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.getDate
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ActivityTypeViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ChildViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityTypeOverviewScreen(
    modifier: Modifier = Modifier,
    activityKey: String,
    childStage: String,
    categoryKey: String,
    childId: String,
    activityTypeViewModel: ActivityTypeViewModel = hiltViewModel(),
    childViewModel: ChildViewModel = hiltViewModel(),
    quizViewModel: QuizViewModel = hiltViewModel(),
    navController: NavController,
    onLeaderBoardSelected: (childId: String, category: String, childStage: String, difficulty: String?) -> Unit,
    onActivityRuleSelected: (childId: String, categoryKey: String, childStage: String, activityTypeKey: String, selectedDifficulty: String, lastScore: String, lastPlayed: String) -> Unit
) {

    val TAG = "ActivityTypeOverviewScreen"

    val category = getCategoryByKey(categoryKey)
    val activityType = getActivityTypeByKey(activityKey)

    val childDetails by childViewModel.childDetails.observeAsState()
    val gameDetails by activityTypeViewModel.gameDetails.observeAsState()

    val scoreDetails by quizViewModel.scores.observeAsState()


    val gameDetail = remember {
        mutableStateOf<Map<String, String>?>(mapOf())
    }

    // Fetch child details when the composable is first composed
    LaunchedEffect(Unit) {
        childViewModel.fetchChildDetails(childId)
        activityTypeViewModel.fetchChildDetails(childId, categoryKey)
    }

    gameDetail.value = gameDetails.let { detail ->
        when (activityKey) {
            "quiz" -> {
                detail?.quiz
            }

            "game" -> {
                detail?.game
            }

            "outdoor_task" -> {
                detail?.outdoorTask
            }

            "video" -> {
                detail?.game
            }

            else -> {
                mapOf()
            }
        }
    }


    // UI components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CustomTopAppBar(
            title =
            activityType?.title ?: run { "Loading..." },
            onBackClick = { navController.popBackStack() },
            actions = {
                IconButton(onClick = {
                    onLeaderBoardSelected(childId, categoryKey, childStage, null)
                }) {
                    Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard")
                }
                IconButton(onClick = {
                    navController.popBackStack(
                        Screen.ParentHome.route,
                        true
                    )/* Navigate to home */
                }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Category: ${category?.title ?: "Loading..."}",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = Color(0xFF33691E) // Vibrant green color
            )
            Text(
                text = "Gender: ${childDetails?.childGender?.take(1) ?: "?"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF444444) // Grayish text color
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Stage: ${childDetails?.childStage ?: "Loading..."}",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            color = Color(0xFF33691E) // Vibrant green color
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = activityType?.description ?: "Loading...",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF444444) // Grayish text color
        )
        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Card containing last played information
            when (activityKey) {
                "quiz" -> {
                    quizViewModel.fetchScores(childId, categoryKey, childStage, null)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    ) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            /*gameDetail.value?.get("lastPlayedTime").let { timePlayed ->
                                Log.d(TAG, "ActivityTypeOverviewScreen: $timePlayed")
                                Text(
                                    text = "Last Played: ${
                                        getDate(
                                            timePlayed!!.toLong(),
                                            "EEE, dd MMM yyyy | HH:mm"
                                        )
                                    }"
                                )
                            } ?: run { Text("N/A") }*/
                            scoreDetails?.let { scores ->
                                val latestScore = scores.maxByOrNull { it.datePlayed }
                                val highScore = scores.maxByOrNull { it.score }?.score

                                latestScore?.let { score ->
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            "Latest Score Details",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = 18.sp
                                            ),
                                            color = Color(0xFF33691E) // Vibrant green color
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Score: ${score.score}" + if (score.score == highScore) " (high score)" else "",
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF444444) // Grayish text color
                                        )
                                        Text(
                                            "Category: ${HelpMe.revertFromSnakeCase(score.category)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF444444) // Grayish text color
                                        )
                                        Text(
                                            "Stage: ${score.childStage}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF444444) // Grayish text color
                                        )
                                        Text(
                                            "Difficulty: ${score.difficulty}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF444444) // Grayish text color
                                        )
                                        Text(
                                            "Date Played: ${
                                                getDate(
                                                    score.datePlayed.toLong(),
                                                    "EEE dd MMM yyyy"
                                                )
                                            }",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF444444) // Grayish text color
                                        )
                                    } ?: run {
                                        Text(
                                            "No scores available.",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
//                            Text(text = "Last Level Played: ${gameDetail.value?.get("lastLevel") ?: "N/A"}")
//                            Text(text = "Last Score: ${gameDetail.value?.get("lastScore") ?: "N/A"}")
                            }
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Horizontal list of difficulty cards
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(difficultyLevels) { difficulty ->
                    val relevantScores = scoreDetails?.filter { it.difficulty == difficulty.name }
                    val lastScore = relevantScores?.maxByOrNull { it.datePlayed }?.score
                    val highScore = relevantScores?.maxByOrNull { it.score }?.score
                    val lastPlayed =
                        relevantScores?.maxByOrNull { it.datePlayed }?.datePlayed


                    Card(
                        modifier = Modifier
                            .height(150.dp)
                            .clickable {
                                onActivityRuleSelected(
                                    childId,
                                    categoryKey,
                                    childStage,
                                    activityKey,
                                    difficulty.name,
                                    (lastScore ?: "0").toString(),
                                    lastPlayed ?: System
                                        .currentTimeMillis()
                                        .toString()
                                )
                            },
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = difficulty.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                color = when (difficulty.name) {
                                    "Easy" -> Color.Green
                                    "Medium" -> Color.Yellow
                                    "Hard" -> Color.Red
                                    else -> Color.Transparent
                                }
                            )
                            Text(
                                text = "Last Score: ${lastScore ?: "N/A"} ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            Text(
                                text = "High Score: ${highScore ?: "N/A"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )

                            val lottieFile = when (difficulty.name) {
                                "Easy" -> R.raw.easy
                                "Medium" -> R.raw.medium
                                "Hard" -> R.raw.hard
                                else -> R.raw.earlychildhood // Provide a default animation or handle unknown activity types
                            }

                            LottieAnimationView(
                                lottieFile = lottieFile,
                                modifier = Modifier
                                    .size(48.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }

    }
}


// Assuming these are your data classes
data class DifficultyLevel(
    val name: String,
    val lastScore: Int,
    val highScore: Int
)

// Sample data
val difficultyLevels = listOf(
    DifficultyLevel("Easy", lastScore = 80, highScore = 100),
    DifficultyLevel("Medium", lastScore = 70, highScore = 95),
    DifficultyLevel("Hard", lastScore = 60, highScore = 90)
)