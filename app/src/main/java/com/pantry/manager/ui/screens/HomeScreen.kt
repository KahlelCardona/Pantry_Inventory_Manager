package com.pantry.manager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pantry.manager.data.model.Ingredient
import com.pantry.manager.data.model.StorageLocation
import com.pantry.manager.ui.theme.PantryInventoryManagerTheme
import com.pantry.manager.ui.viewmodel.HomeScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

@Composable
fun HomeScreen(
    onNavigateToScanner: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel()
) {
    val ingredients by viewModel.ingredients.collectAsState()

    HomeScreenContent(
        ingredients = ingredients,
        onNavigateToScanner = onNavigateToScanner,
        onAddIngredient = { viewModel.addIngredient(it) },
        onRemoveIngredient = { viewModel.deleteIngredient(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    ingredients: List<Ingredient>,
    onNavigateToScanner: () -> Unit,
    onAddIngredient: (Ingredient) -> Unit,
    onRemoveIngredient: (Ingredient) -> Unit
) {
    var selectedLocationFilter by remember { mutableStateOf<StorageLocation?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredIngredients = ingredients.filter {
        selectedLocationFilter == null || it.storageLocation == selectedLocationFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Pantry & Fridge") },
                actions = {
                    IconButton(onClick = onNavigateToScanner) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan Barcode")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Item") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            val expiringCount = ingredients.count { ceil((it.expirationDateMillis - System.currentTimeMillis()) / 86400000.0).toInt() <= 3 }
            if (expiringCount > 0) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$expiringCount item(s) expiring within 3 days! Plan a meal soon.",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Text("Filter Storage Area:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                FilterChip(
                    selected = selectedLocationFilter == null,
                    onClick = { selectedLocationFilter = null },
                    label = { Text("All") }
                )
                StorageLocation.entries.forEach { location ->
                    FilterChip(
                        selected = selectedLocationFilter == location,
                        onClick = { selectedLocationFilter = location },
                        label = { Text(location.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredIngredients.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No ingredients found in this section.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredIngredients, key = { it.id }) { item ->
                        IngredientCard(
                            ingredient = item,
                            onRemove = { onRemoveIngredient(item) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        ManualAddIngredientDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newItem ->
                onAddIngredient(newItem)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun IngredientCard(
    ingredient: Ingredient,
    onRemove: () -> Unit
) {
    val daysLeft = ceil((ingredient.expirationDateMillis - System.currentTimeMillis()) / (86400000.0)).toInt()
    val isExpiring = daysLeft <= 3

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(ingredient.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Quantity: ${ingredient.quantity} • Location: ${ingredient.storageLocation}")
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = if (isExpiring) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (daysLeft < 0) "Expired!" else "Expires in $daysLeft day(s)",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isExpiring) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove Item", tint = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualAddIngredientDialog(
    onDismiss: () -> Unit,
    onAdd: (Ingredient) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var location by remember { mutableStateOf(StorageLocation.PANTRY) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis() + (86400000L * 7)
    )

    val selectedDateMillis = datePickerState.selectedDateMillis ?: (System.currentTimeMillis() + (86400000L * 7))
    val formattedDate = java.time.Instant.ofEpochMilli(selectedDateMillis).atZone(java.time.ZoneOffset.UTC).format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy", java.util.Locale.getDefault()))

    val daysLeft = ceil((selectedDateMillis - System.currentTimeMillis() - java.util.TimeZone.getDefault().getOffset(selectedDateMillis)) / (86400000.0)).toInt().coerceAtLeast(0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Ingredient Manually") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name (e.g. Eggs)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity (e.g. 1 dozen)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = "$formattedDate ($daysLeft days remaining)",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Expiration Date") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Expiration Date")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                )
                Text("Storage Location:", fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StorageLocation.entries.forEach { loc ->
                        FilterChip(
                            selected = location == loc,
                            onClick = { location = loc },
                            label = { Text(loc.name.take(3)) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(
                            Ingredient(
                                id = 0L,
                                name = name,
                                quantity = if (quantity.isBlank()) "1 unit" else quantity,
                                storageLocation = location,
                                expirationDateMillis = selectedDateMillis - java.util.TimeZone.getDefault().getOffset(selectedDateMillis)
                            )
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PantryInventoryManagerTheme {
        HomeScreenContent(
            ingredients = listOf(
                Ingredient(1, "Whole Milk", "1 Gallon", StorageLocation.FRIDGE, System.currentTimeMillis() + 86400000L * 2, "12345678", 150, 8f, 12f, 8f),
                Ingredient(2, "Chicken Breast", "2 lbs", StorageLocation.FREEZER, System.currentTimeMillis() + 86400000L * 14, "87654321", 165, 31f, 0f, 3.6f),
                Ingredient(3, "Fresh Spinach", "1 Bag", StorageLocation.FRIDGE, System.currentTimeMillis() + 86400000L * 1, "11223344", 23, 2.9f, 3.6f, 0.4f),
                Ingredient(4, "Canned Tomatoes", "3 Cans", StorageLocation.PANTRY, System.currentTimeMillis() + 86400000L * 180, "44332211", 50, 1f, 10f, 0f)
            ),
            onNavigateToScanner = {},
            onAddIngredient = {},
            onRemoveIngredient = {}
        )
    }
}

