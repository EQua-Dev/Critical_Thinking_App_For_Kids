package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.LottieAnimationView
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.ActivityType
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Category

@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                color = getRandomColor() // Random color for the title
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ActivityCard(
    activity: ActivityType,
    onActivityTypeClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onActivityTypeClicked(activity.activityTypeKey)/* Handle click */ }
            .fillMaxWidth()
            .padding(4.dp)
            .aspectRatio(1f), // Ensures the button is square-shaped
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp) // Add some padding inside the card
        ) {


            val lottieFile = when (activity.activityTypeKey) {
                "quiz" -> R.raw.quiz
                "game" -> R.raw.game
                "video" -> R.raw.video
                "outdoor_task" -> R.raw.outdoor
                else -> R.raw.earlychildhood // Provide a default animation or handle unknown activity types
            }
            // Consider adding an icon here for the activity type

            LottieAnimationView(
                lottieFile = lottieFile,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = activity.title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp), // Larger and bolder title
                color = Color(0xFF33691E) // Vibrant title color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFF444444) // Grayish text color for better contrast
            )
        }
    }
}

// Function to generate a random color
@Composable
private fun getRandomColor(): Color {
    val colors = listOf(
        Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Red, Color.Blue, Color.Cyan
    )
    return colors.random()
}


