package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.ui.theme.Typography

@Composable
fun EmptyListView(modifier: Modifier = Modifier, message: String) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun OtpBottomSheet(
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit,
    errorMessage: String?
) {
    val otp = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter OTP", style = Typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = otp.value,
            onValueChange = { otp.value = it },
            label = { Text("OTP") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = errorMessage != null
        )

        if (errorMessage != null) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onVerifyOtp(otp.value) }) {
            Text("Verify")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { onResendOtp() }) {
            Text("Resend OTP")
        }
    }
}

