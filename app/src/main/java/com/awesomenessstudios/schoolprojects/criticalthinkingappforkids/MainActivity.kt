package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.ui.theme.CriticalThinkingAppForKidsTheme
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.holder.HolderScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.providers.LocalNavHost
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.LocalScreenSize
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.getScreenSize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val defaultStatusBarColor = MaterialTheme.colorScheme.background.toArgb()
            var statusBarColor by remember { mutableStateOf(defaultStatusBarColor) }
            window.statusBarColor = statusBarColor

            /** Our navigation controller */
            val navController = rememberNavController()

            /** Getting screen size */
            val size = LocalContext.current.getScreenSize()
            CriticalThinkingAppForKidsTheme(

            ) {
                CompositionLocalProvider(
                    LocalScreenSize provides size,
                    LocalNavHost provides navController
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HolderScreen(
                            onStatusBarColorChange = {
                                //** Updating the color of the status bar *//*
                                //** Updating the color of the status bar *//*
                                //** Updating the color of the status bar *//*

                                //** Updating the color of the status bar *//*
                                statusBarColor = it.toArgb()
                            }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CriticalThinkingAppForKidsTheme {
        Greeting("Android")
    }
}