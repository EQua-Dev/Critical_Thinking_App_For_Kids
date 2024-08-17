package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.categories
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components.CategoryCard
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ChildViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildHomeScreen(
    childId: String,
    navController: NavController,
    onNavigationRequested: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    childViewModel: ChildViewModel = hiltViewModel(),
    onCategorySelected: (childId: String, childStage: String, category: String) -> Unit
) {

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
            title = { Text(text = childDetails?.childName ?: "Loading...") },
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
            Text(text = "Category: ${childDetails?.childStage ?: "Loading..."}")
            Text(
                text = "Gender: ${childDetails?.childGender?.take(1) ?: "?"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome ${childDetails?.childName ?: "Guest"}!",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = getCategoryDescription(childDetails?.childStage),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display categories as clickable cards (as per previous example)
        // You might need to adjust this based on your full design requirements

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(categories) { category ->
                CategoryCard(category = category) {
                    childDetails?.let {
                        onCategorySelected(
                            it.childId,
                            childDetails?.childStage!!,
                            category.categoryKey
                        )
                    }

                    // Handle category click, e.g., navigate to a new screen
                    //navController.navigate("category_details/${category.title}")
                }
            }
        }
    }
}

@Composable
fun getCategoryDescription(category: String?): String {
    return when (category) {
        "Early Childhood" -> "Ignite curiosity and build foundational skills with fun and engaging activities designed for the youngest learners."
        "Primary" -> "Foster essential academic and social skills through interactive and age-appropriate challenges that promote growth and learning."
        "Pre-Teen" -> "Prepare for the teenage years with activities that enhance critical thinking, problem-solving, and creativity, setting the stage for future success."
        "Teenager" -> "Empower teenagers with advanced challenges that sharpen decision-making, logical reasoning, and personal development, readying them for adulthood."
        else -> "No description available."
    }
}