package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Child

@Composable
fun ChildItem(child: Child) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = child.childName)
            Text(text = "Age: ${child.childAge}")
            Text(text = "Gender: ${child.childGender}")
            Text(text = "Category: ${child.childCategory}")
        }
    }
}
