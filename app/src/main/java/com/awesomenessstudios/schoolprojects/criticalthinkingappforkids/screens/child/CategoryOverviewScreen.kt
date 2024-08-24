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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.ActivityType
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.getCategoryByKey
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components.ActivityCard
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components.CustomTopAppBar
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.ui.theme.Typography
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ChildViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryOverviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    childId: String,
    categoryKey: String,
    childStage: String,
    onActivityTypeSelected: (activityTypeKey: String, childStage: String, category: String, childId: String) -> Unit,
    onLeaderBoardSelected: (childId: String, category: String, childStage: String, difficulty: String?) -> Unit,
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
            .padding(8.dp)
    ) {

        CustomTopAppBar(
            title =
            category?.title ?: run { childDetails?.childName ?: "Loading..." },
            onBackClick = { navController.popBackStack() },
            actions = {
                IconButton(onClick = {
                    onLeaderBoardSelected(childId, categoryKey, childStage, null)
                }) {
                    Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = childDetails?.childName ?: "Loading...",
                        style = Typography.headlineSmall
                    )

                    val childGender = childDetails?.childGender?.take(1) ?: "?"

                    Text(
                        text = when (childGender) {
                            "M" -> "👦🏼"
                            "F" -> "👧🏼"
                            else -> "🧒🏼"

                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = category?.description ?: "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Choose an Activity",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF33691E),
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.padding(16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)

                ) {
                    items(activities)
                    { activity ->
                        ActivityCard(activity = activity) { activityType ->
                            // ... your activity selection logic
                            onActivityTypeSelected(activityType, childId, categoryKey, childStage)
                            Toast.makeText(context, activityType, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}




