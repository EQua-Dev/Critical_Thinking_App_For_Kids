package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.R
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.LottieAnimationView
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.categories
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components.CategoryCard
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.components.CustomTopAppBar
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ChildViewModel
import kotlinx.coroutines.delay

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
    var showContent by remember { mutableStateOf(false) }

    // Fetch child details when the composable is first composed
    LaunchedEffect(childId) {
        childViewModel.fetchChildDetails(childId)
    }
    // Start the Lottie animation and show the content after it finishes
    LaunchedEffect(Unit) {
        delay(5000) // Adjust this duration to match your Lottie animation length
        showContent = true
    }
    if (!showContent) {
        // Show Lottie animation while loading
        val lottieAnimations = listOf(
            R.raw.earlychildhood,
            R.raw.primary,
            R.raw.preteen,
            R.raw.teen,
        )

        fun getRandomLottieAnimation(): Int {
            return lottieAnimations.random()
        }

        LottieAnimationView(
            lottieFile = getRandomLottieAnimation(),
            modifier = Modifier.fillMaxSize()
        )

    } else {
        // UI components
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CustomTopAppBar(
                title =
                childDetails?.childName,
                onBackClick = { navController.popBackStack() },
                actions = {
                    null
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)) // Light background color
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement
                        = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Category: ${childDetails?.childStage ?: "Loading..."}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            color = Color(0xFF33691E) // Vibrant green color
                        )
                        val childGender = childDetails?.childGender?.take(1) ?: "?"
                        Text(
                            text = "Gender: ${
                                when (childGender) {
                                    "M" -> "👦🏼"
                                    "F" -> "👧🏼"
                                    else -> "🧒🏼"

                                }
                            }",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF444444) // Grayish text color
                        )
                    }

                    Text(
                        text = "Welcome ${childDetails?.childName ?: "Guest"}!",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = getCategoryDescription(childDetails?.childStage),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

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