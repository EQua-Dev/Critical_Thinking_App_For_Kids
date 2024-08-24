package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Child

@Composable
fun ChildItem(child: Child, onChildClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onChildClick(child.childId) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)) // Light background color
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = child.childName,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = Color(0xFF33691E) // Vibrant green color
            )
            Text(
                text = "Age: ${child.childAge}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF444444) // Grayish text color
            )
            Text(
                text = "Gender: ${child.childGender}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF444444) // Grayish text color
            )
            Text(
                text = "Category: ${child.childStage}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF444444) // Grayish text color
            )
        }
    }
}
