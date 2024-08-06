package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun showLogoutConfirmationDialog(onConfirm: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { onConfirm(false) },
        title = {
            Text(text = "Logout")
        },
        text = {
            Text(text = "Are you sure you want to logout?")
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(true) }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { onConfirm(false) }) {
                Text("No")
            }
        }
    )
}

