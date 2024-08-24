package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.auth

import CustomTextField
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation.Screen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.AuthViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.CustomSnackbar
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.FlatButton
import com.schoolprojects.caribank.components.DropdownField
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.PasswordStrengthIndicator
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.components.OtpBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onNavigationRequested: (String, Boolean) -> Unit,
    onAccountCreated: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val TAG = "SignUpScreen"

    val context = LocalContext.current as ComponentActivity
    var showSnackbar by remember { mutableStateOf(false) }
    var showOtpBottomSheet by remember { mutableStateOf(false) }

    val otpError by authViewModel.otpError.observeAsState()

    val errorMessage = remember {
        mutableStateOf("")
    }
    val firstName by remember { authViewModel.studentFirstName }
    val lastName by remember { authViewModel.studentLastName }
    val email by remember { authViewModel.email }
    val phoneNumber by remember { authViewModel.phoneNumber }
    val password by remember { authViewModel.password }
    val confirmPassword by remember { authViewModel.confirmPassword }
    val passwordStrength by remember { authViewModel.passwordStrength }
    val selectedGender by remember { authViewModel.gender }

    val genders = listOf("Male", "Female", "Custom")


    val showLoading = remember { authViewModel.showLoading }



    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = firstName,
                onValueChange = { authViewModel.updateFirstName(it) },
                label = "First Name",
                placeholder = "First Name",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = lastName,
                onValueChange = { authViewModel.updateLastName(it) },
                label = "Last Name",
                placeholder = "Last Name",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = email,
                onValueChange = { authViewModel.updateEmail(it) },
                label = "Email",
                placeholder = "Email",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = password,
                onValueChange = { authViewModel.updatePassword(it) },
                label = "Password",
                placeholder = "Password",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )
            PasswordStrengthIndicator(passwordStrength)
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextField(
                value = confirmPassword,
                onValueChange = { authViewModel.updateConfirmPassword(it) },
                label = "Confirm Password",
                placeholder = "Retype Password",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = phoneNumber,
                onValueChange = { authViewModel.updatePhoneNumber(it) },
                label = "Phone Number",
                placeholder = "Phone Number",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                selectedValue = selectedGender,
                onValueChange = { authViewModel.updateGender(it) },
                label = "Gender",
                options = genders,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Gender"
            )
            Spacer(modifier = Modifier.height(8.dp))


            FlatButton(
                text = "Sign Up",
                onClick = {
                    authViewModel.updateLoadingStatus(true)
                    val username = "$firstName $lastName"
                    authViewModel.signUp(
                        username = username,
                        email = email,
                        phoneNumber = phoneNumber,
                        gender = selectedGender,
                        password = password,
                        confirmPassword = confirmPassword,
                        callback = { status, message ->

                            Log.d(TAG, "SignUpScreen: $status")
                            Log.d(TAG, "SignUpScreen: $message")
                            if (status) {
                                authViewModel.sendOtp(
                                    context,
                                    phoneNumber,
                                    callback = { sendOtpStatus, sendOtpMessage ->

                                        // if (sendOtpStatus) {
                                        // Show OTP Bottom Sheet
                                        showOtpBottomSheet = true
                                        authViewModel.sendOtp(
                                            context,
                                            phoneNumber
                                        ) { success, message ->
                                            if (!success) {
                                                errorMessage.value = message
                                                showSnackbar = true
                                            }
                                        }
                                        //}
                                    })
                            } else {
                                errorMessage.value = message
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                            authViewModel.updateLoadingStatus(false)
                        },
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { onNavigationRequested(Screen.Login.route, false) }) {
                    Text(" Have Account? Login Instead")
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
        if (showLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }

    }


    if (showOtpBottomSheet) {
        val bottomSheetState = rememberModalBottomSheetState(true)
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                showOtpBottomSheet = false
                authViewModel.clearOtpError()
            }
        ) {
            OtpBottomSheet(
                onVerifyOtp = { otp ->
                    Log.d(TAG, "SignUpScreen: $otp")
                    authViewModel.verifyOtp(otp) { success, message ->
                        if (success) {
                            showOtpBottomSheet = false
                            authViewModel.saveParent { b, s ->
                                if (b) {
                                    onNavigationRequested(Screen.Login.route, true)
                                }
                            }
                        } else {
                            errorMessage.value = message
                        }
                    }
                },
                onResendOtp = {
                    authViewModel.sendOtp(context, phoneNumber) { success, message ->
                        if (!success) {
                            errorMessage.value = message
                        }
                    }
                },
                errorMessage = otpError
            )
        }
    }
}
