package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.auth

import CustomTextField
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.R
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation.Screen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.AuthViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.CustomSnackbar
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.FlatButton

@Composable
fun LoginScreen(
    onNavigationRequested: (String, Boolean) -> Unit,
    onAuthenticated: (String) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {


    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val email by remember { authViewModel.email }
    val password by remember { authViewModel.password }

    val errorMessage = remember { mutableStateOf("") }
    val showLoading by remember { mutableStateOf(authViewModel.showLoading) }
    var showSnackbar by remember { mutableStateOf(false) }

    val userLocation by authViewModel.userLocation.observeAsState()

    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            authViewModel.fetchUserLocation(context)
        } else {
            // Handle permission denial
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text
                = "Login",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary // Vibrant green color
            )

            Spacer(modifier = Modifier.height(32.dp))

            CustomTextField(
                value = email,
                onValueChange = { authViewModel.updateEmail(it) },
                label = "Email",
                placeholder = "Enter your email",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = password,
                onValueChange = { authViewModel.updatePassword(it) },
                label = "Password",
                placeholder = "Enter your password",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            FlatButton(
                onClick = {
                    authViewModel.updateLoadingStatus(true)
                    authViewModel.loginUser(
                        email,
                        password,
                        userLocation!!,
                        onSuccess = {
                            authViewModel.updateLoadingStatus(false)
                            onNavigationRequested(Screen.ParentHome.route, true)
                        },
                        onFailure = { exception ->
                            authViewModel.updateLoadingStatus(false)
                            errorMessage.value = exception.message ?: "Login failed"
                        }
                    )
                },
                contentColor = Color(0xFFFFFFFF), // Vibrant green button
                backgroundColor = Color(0xFF33691E), // Vibrant green button
                modifier = Modifier.fillMaxWidth(), text = "Login"
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()

            ) {
                TextButton(
                    onClick
                    = { onNavigationRequested(Screen.ForgotPassword.route, false) },
                    contentPadding = PaddingValues(0.dp) // Remove default padding
                ) {
                    Text("Forgot Password?")
                    // Adjust color based on your theme
                }

                TextButton(
                    onClick = { onNavigationRequested(Screen.Signup.route, false) },
                    contentPadding = PaddingValues(0.dp) // Remove default padding
                ) {
                    Text("Create Account")
                    // Adjust color based on your theme
                }
            }
        }
        if (showLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))

            }
        }

        if (showSnackbar) {
            CustomSnackbar(
                message = errorMessage.value,
                actionLabel = "",
                onActionClick = {
                    // Handle action click
                    showSnackbar = false
                },
                onDismiss = {
                    // Handle dismiss
                    showSnackbar = false
                }
            )
        }

    }
}