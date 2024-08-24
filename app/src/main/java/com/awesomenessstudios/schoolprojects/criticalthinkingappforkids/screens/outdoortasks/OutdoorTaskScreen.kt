package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.outdoortasks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.OutdoorTask
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.OutdoorTaskViewModel

@Composable
fun OutdoorTaskScreen(
    viewModel: OutdoorTaskViewModel = hiltViewModel(),
    childStage: String,
    difficulty: String,
    category: String
) {
    val TAG = "OutdoorTaskScreen"
    val tasks by viewModel.tasks.observeAsState(emptyList())

    Log.d(TAG, "OutdoorTaskScreen: $childStage $difficulty $category")

    LaunchedEffect(Unit) {
        viewModel.fetchOutdoorTasks(childStage, difficulty, category)
    }

    tasks.firstOrNull()?.let { task ->
        TaskDetail(task, viewModel)
    } ?: run {
        Text("No tasks available for this selection.")
    }
}


@Composable
fun TaskDetail(task: OutdoorTask, viewModel: OutdoorTaskViewModel) {
    val totalTime = viewModel.convertDurationToMillis(task.duration)
    val timeRemaining by viewModel.timeRemaining.collectAsState()

    LaunchedEffect(task) {
        viewModel.startCountdown(totalTime)
    }

    val progress = if (totalTime > 0) (totalTime - timeRemaining) / totalTime.toFloat() else 1f


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            task.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF33691E),
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)

        ) {
            Icon(
                imageVector = Icons.Default.Alarm,
                contentDescription = "Duration",
                tint = Color.Green
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Duration",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF388E3C),
                    fontSize = 18.sp
                )
            )
        }

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {

                Text(
                    task.duration,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF388E3C),
                    )
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = "Benefit",
                tint = Color.Cyan
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Benefit",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF33691E),
                    fontSize = 18.sp
                )
            )
        }
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {

                Text(
                    task.benefit,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF33691E),
                    )
                )


            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Task,
                contentDescription = "Task",
                tint = Color.Magenta
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Task",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF33691E),
                    fontSize = 18.sp
                )
            )
        }

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {

                Text(
                    task.description,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF33691E),
                    )
                )


            }
        }

        Text(
            "Time Remaining: ${viewModel.formatTime(timeRemaining)}",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Red),
            modifier = Modifier.padding(16.dp)
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.Gray.copy(alpha = 0.3f))
        )
    }
}
