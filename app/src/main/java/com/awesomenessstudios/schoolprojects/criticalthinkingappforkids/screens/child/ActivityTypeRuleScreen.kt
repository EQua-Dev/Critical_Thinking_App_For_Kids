package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.Button
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
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.HelpMe
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ActivityTypeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityTypeRuleScreen(
    modifier: Modifier = Modifier, activityTypeKey: String,
    childStage: String,
    categoryKey: String,
    childId: String,
    selectedDifficulty: String,
    onQuizStart: (
        childId: String,
        categoryKey: String,
        childStage: String,
        difficultyLevel: String
    ) -> Unit,
    onGameStart: (
        childId: String,
        category: String,
        difficultyLevel: String,
        childStage: String
    ) -> Unit,
    onVideoSelected: (childStage: String, category: String) -> Unit,
    navController: NavController,
    activityTypeViewModel: ActivityTypeViewModel = hiltViewModel()
) {


    val category = getCategoryByKey(categoryKey)
    val activityType = getActivityTypeByKey(activityTypeKey)

    val gameDetails by activityTypeViewModel.gameDetails.observeAsState()


    val gameDetail = remember {
        mutableStateOf<Map<String, String>?>(mapOf())
    }
    // Fetch child details when the composable is first composed
    LaunchedEffect(Unit) {
        activityTypeViewModel.fetchChildDetails(childId, categoryKey)
    }

    gameDetail.value = gameDetails.let { detail ->
        when (activityTypeKey) {
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



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TopAppBar(
            title = {
                Text(
                    text = selectedDifficulty,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Back")
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

        Button(onClick = {
            /*
            childId
category
difficultyLevel
childStage
            * */
            when (activityTypeKey) {
                "quiz" -> {
                    onQuizStart(childId, categoryKey, childStage, selectedDifficulty)
                }

                "game" -> {

                    onGameStart(childId, categoryKey, selectedDifficulty, childStage)
                }

                "video" -> {
                    val stageOfChild = HelpMe.convertToSnakeCase(childStage)
                    onVideoSelected(stageOfChild, categoryKey)
                }

            }
        }) {
            Text(text = "Start")
        }
    }


}