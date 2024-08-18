package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.getActivityTypeByKey
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.getCategoryByKey
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.getDate
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ActivityTypeViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ChildViewModel

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
    navController: NavController,
    onActivityRuleSelected: (childId: String, categoryKey: String, childStage: String, activityTypeKey: String, selectedDifficulty: String) -> Unit
) {

    val TAG = "ActivityTypeOverviewScreen"

    val category = getCategoryByKey(categoryKey)
    val activityType = getActivityTypeByKey(activityKey)

    val childDetails by childViewModel.childDetails.observeAsState()
    val gameDetails by activityTypeViewModel.gameDetails.observeAsState()

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
        TopAppBar(
            title = {
                activityType?.let {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } ?: run { Text(text = "Loading...") }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Navigate to leaderboard */ }) {
                    Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard")
                }

                IconButton(onClick = { /* Navigate to home */ }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Category: ${category?.title ?: "Loading..."}")
            Text(
                text = "Gender: ${childDetails?.childGender?.take(1) ?: "?"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Stage: ${childDetails?.childStage ?: "Loading..."}")
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = activityType?.description ?: "Loading...",
            style = MaterialTheme.typography.bodyLarge
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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

                    Text(text = "Last Level Played: ${gameDetail.value?.get("lastLevel") ?: "N/A"}")
                    Text(text = "Last Score: ${gameDetail.value?.get("lastScore") ?: "N/A"}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Horizontal list of difficulty cards
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(difficultyLevels) { difficulty ->
                    Card(
                        modifier = Modifier
                            .height(150.dp)
                            .clickable {
                                onActivityRuleSelected(childId, categoryKey, childStage, activityKey,difficulty.name)
                            },
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = difficulty.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Last Score: ${difficulty.lastScore}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "High Score: ${difficulty.highScore}",
                                style = MaterialTheme.typography.bodySmall
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