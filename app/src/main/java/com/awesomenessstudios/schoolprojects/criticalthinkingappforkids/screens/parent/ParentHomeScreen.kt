package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.parent

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.AddChildBottomSheet
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components.ChildItem
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation.Screen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.ui.theme.Typography
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ParentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentHomeScreen(
    modifier: Modifier = Modifier,
    onNavigationRequested: (String, Boolean) -> Unit,
    parentViewModel: ParentViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val username = parentViewModel.userName.value
    val children by parentViewModel.children.collectAsState()

    val openDialog by remember { mutableStateOf(parentViewModel.openDialog) }
    val addChildDialog by remember { mutableStateOf(parentViewModel.addChildDialog) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "MindSpark")
                },
                actions = {
                    Text(
                        text = "Hello, $username",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = Typography.bodyLarge
                    )
                    IconButton(onClick = { /* Navigate to Logs Screen */ }) {
                        Icon(Icons.Filled.List, contentDescription = "Logs")
                    }
                    IconButton(onClick = {
                        // Show logout confirmation dialog
                        parentViewModel.updateDialogStatus()
                    }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { parentViewModel.updateAddChildDialogStatus() }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Child")
            }
        })
    { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Children",
                    style = Typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (children.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "No child, Click Add Child button to add a child",
                            modifier = Modifier.padding(16.dp),
                            style = Typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn {
                        items(children) { child ->
                            ChildItem(child)
                        }
                    }
                }
            }
        }
    }


    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            title = {
                Text(text = "Logout", style = Typography.titleLarge)
            },
            text = {
                Text(text = "Do you want to logout?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        parentViewModel.logout {
                            onNavigationRequested(Screen.Login.route, true)
                            parentViewModel.updateDialogStatus()
                        }


                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        parentViewModel.updateDialogStatus()
                    }
                ) {
                    Text("No")
                }
            },

            )
    }

    if (addChildDialog.value){
        AddChildBottomSheet(onChildAdded = { child ->
            parentViewModel.addChildToFirestore(child, callback = { status, message ->
                if (status){
                    parentViewModel.updateAddChildDialogStatus()
                }else{
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })
        }, onClose = {
            parentViewModel.updateAddChildDialogStatus()
        })
    }

}
