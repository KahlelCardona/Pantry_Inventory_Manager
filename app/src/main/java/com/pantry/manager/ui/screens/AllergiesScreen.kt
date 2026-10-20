package com.pantry.manager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pantry.manager.data.model.Allergy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllergiesScreen() {
    var enableNotifications by remember { mutableStateOf(true) }
    var newAllergyText by remember { mutableStateOf("") }

    var allergyList by remember {
        mutableStateOf(
            listOf(
                Allergy(1, "Peanuts", isEnabled = true),
                Allergy(2, "Lactose / Dairy", isEnabled = true),
                Allergy(3, "Gluten", isEnabled = false),
                Allergy(4, "Shellfish", isEnabled = true)
            )
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Allergies & Settings") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Expiration Alert Settings", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Push Notifications", fontWeight = FontWeight.Bold)
                            Text("Alert when food expires within 3 days", fontSize = 12.sp)
                        }
                    }
                    Switch(
                        checked = enableNotifications,
                        onCheckedChange = { enableNotifications = it }
                    )
                }
            }

            HorizontalDivider()

            Text("My Allergies & Dietary Restrictions", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newAllergyText,
                    onValueChange = { newAllergyText = it },
                    label = { Text("Add Allergy (e.g. Soy)") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (newAllergyText.isNotBlank()) {
                            allergyList = allergyList + Allergy(
                                id = System.currentTimeMillis(),
                                allergyName = newAllergyText,
                                isEnabled = true
                            )
                            newAllergyText = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(allergyList, key = { it.id }) { allergy ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(allergy.allergyName, fontWeight = FontWeight.Medium)
                            Switch(
                                checked = allergy.isEnabled,
                                onCheckedChange = { isChecked ->
                                    allergyList = allergyList.map {
                                        if (it.id == allergy.id) it.copy(isEnabled = isChecked) else it
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
