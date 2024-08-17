package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Leaderboard
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.ActivityType
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.getCategoryByKey
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components.ActivityCard
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ChildViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryOverviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    childId: String,
    categoryKey: String,
    childStage: String,
    onActivityTypeSelected: (activityTypeKey: String,  childStage: String, category: String, childId: String,) -> Unit,
    childViewModel: ChildViewModel = hiltViewModel()
) {

    val category = getCategoryByKey(categoryKey)
    val context = LocalContext.current


    val activities = listOf(
        ActivityType.Quiz,
        ActivityType.Game,
        ActivityType.OutdoorTask,
        ActivityType.Video
    )

    val childDetails by childViewModel.childDetails.observeAsState()

    // Fetch child details when the composable is first composed
    LaunchedEffect(childId) {
        childViewModel.fetchChildDetails(childId)
    }

    // UI components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = {
                category?.let {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } ?: run { Text(text = childDetails?.childName ?: "Loading...") }
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
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = childDetails?.childName ?: "Loading...")
            Text(
                text = "Gender: ${childDetails?.childGender?.take(1) ?: "?"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = category?.description ?: "Loading...",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Choose an Activity",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(activities) { activity ->
                    ActivityCard(activity = activity) { activityType ->
                        onActivityTypeSelected(activityType, childId, categoryKey, childStage)
                        Toast.makeText(context, activityType, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}

