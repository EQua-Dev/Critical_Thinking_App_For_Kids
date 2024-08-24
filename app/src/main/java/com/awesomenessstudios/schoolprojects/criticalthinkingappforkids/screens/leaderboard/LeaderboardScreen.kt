package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.leaderboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.R
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.LeaderboardEntry
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation.Screen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components.CustomTopAppBar
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    childId: String,
    category: String,
    childStage: String,
    difficulty: String? = null,
    navController: NavController,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {

    val scores by viewModel.scores.collectAsState()
    val leaderboardEntries by viewModel.leaderboardEntries.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }

    val difficulties = listOf("Easy", "Medium", "Hard")


    LaunchedEffect(Unit) {
        viewModel.fetchScores(childId, category, childStage, difficulty)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        CustomTopAppBar(
            title = stringResource(id = R.string.leaderboard),
            onBackClick = { navController.popBackStack() },
            actions = {
                IconButton(onClick = {
                    navController.popBackStack(
                        Screen.ParentHome.route,
                        true
                    )
                }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
            }
        )
        // Display message before the leaderboard
        Text(
            text = "This is not a ranking of knowledge but just a record of activities. Please do not feel sad if you are not at the top.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Display difficulty or tab bar
        if (difficulty == null) {
            // Display the TabRow only if no specific difficulty is selected
            TabRow(selectedTabIndex = selectedTabIndex) {
                difficulties.forEachIndexed { index, difficulty ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            viewModel.fetchScores(
                                childId,
                                category,
                                childStage,
                                difficulties[selectedTabIndex]
                            )
                        },
                        text = { Text(text = difficulty) }
                    )
                }
            }
        } else {
            // Display the selected difficulty name if passed
            Text(
                text = difficulty,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Display the leaderboard
        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Rank", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Name", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Score", style = MaterialTheme.typography.bodyLarge)
                }
            }

            itemsIndexed(leaderboardEntries) { index, entry ->
                LeaderboardItem(rank = index + 1, childId = childId, entry = entry)
            }
        }
    }
}

@Composable
fun LeaderboardItem(rank: Int, childId: String, entry: LeaderboardEntry) {
    val isHighlighted = childId == entry.childId
    val rankIcon = when (rank) {
        1 -> Icons.Default.Star // Replace with your gold crown icon
        2 -> Icons.Default.Star // Replace with your silver crown icon
        3 -> Icons.Default.Star // Replace with your bronze crown icon
        else -> null
    }

    // Display rank, child name, and score
    Column {
        Box(
            modifier = Modifier
                .background(
                    if (isHighlighted) MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.1f
                    ) else Color.Transparent
                )
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (rankIcon != null) {
                    Icon(
                        imageVector = rankIcon,
                        contentDescription = "Rank $rank",
                        tint = if (rank == 1) Color(0xFFFFD700) // Gold
                        else if (rank == 2) Color(0xFFC0C0C0) // Silver
                        else Color(0xFFCD7F32) // Bronze
                    )
                } else {
                    Text(text = "$rank", style = MaterialTheme.typography.bodyLarge)
                }


                Text(text = entry.childName, style = MaterialTheme.typography.bodyLarge)

                Text(text = entry.score.toString(), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider()
    }
}

