package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.components

import CustomTextField
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Child
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.ui.theme.Typography
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.mAuth
import com.schoolprojects.caribank.components.DropdownField
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChildBottomSheet(
    onChildAdded: (Child) -> Unit,
    onClose: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var category by remember { mutableStateOf("") }

    val categories = listOf("Early Childhood", "Primary", "Pre-Teen", "Teenager")

    fun determineCategory(age: Int): String {
        return when (age) {
            in 4..6 -> categories[0]
            in 7..9 -> categories[1]
            in 10..12 -> categories[2]
            in 13..16 -> categories[3]
            else -> "Unknown"
        }
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Add Child", style = Typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "Child Name",
                    keyboardType = KeyboardType.Text,
                    placeholder = "Child Name"
                )
                CustomTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = "Age",
                    keyboardType = KeyboardType.Number,
                    placeholder = "Age"
                )
                DropdownField(
                    selectedValue = gender,
                    onValueChange = { gender = it },
                    label = "Gender",
                    placeholder = "Gender",
                    options = listOf("Male", "Female")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val ageInt = age.toIntOrNull() ?: 0
                        val category = determineCategory(ageInt)
                        val child = Child(
                            childId = UUID.randomUUID().toString(),
                            childName = firstName,
                            childGender = gender,
                            childAge = age,
                            childCategory = category,
                            childParent = mAuth.uid ?: "",
                            dateChildCreated = System.currentTimeMillis().toString()
                        )
                        onChildAdded(child)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Child")
                }
            }
        }
    )
}