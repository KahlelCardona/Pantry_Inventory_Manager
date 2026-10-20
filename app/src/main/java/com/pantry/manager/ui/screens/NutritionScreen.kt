package com.pantry.manager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen() {
    var totalCalories by remember { mutableIntStateOf(1250) }
    val calorieGoal = 2000

    var totalProtein by remember { mutableFloatStateOf(65f) }
    val proteinGoal = 120f

    var totalCarbs by remember { mutableFloatStateOf(140f) }
    val carbsGoal = 250f

    var totalFat by remember { mutableFloatStateOf(45f) }
    val fatGoal = 70f

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nutrition Tracker") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Daily Nutrition Summary", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            NutritionProgressBar(
                label = "Calories",
                current = totalCalories.toFloat(),
                goal = calorieGoal.toFloat(),
                unit = "kcal"
            )

            NutritionProgressBar(label = "Protein", current = totalProtein, goal = proteinGoal, unit = "g")
            NutritionProgressBar(label = "Carbohydrates", current = totalCarbs, goal = carbsGoal, unit = "g")
            NutritionProgressBar(label = "Fats", current = totalFat, goal = fatGoal, unit = "g")

            HorizontalDivider()

            Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Button(
                onClick = {
                    totalCalories += 350
                    totalProtein += 20f
                    totalCarbs += 40f
                    totalFat += 10f
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Auto-Log Meal Data According to Serving Sizes")
            }
        }
    }
}

@Composable
fun NutritionProgressBar(
    label: String,
    current: Float,
    goal: Float,
    unit: String
) {
    val progress = (current / goal).coerceIn(0f, 1f)
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Medium)
            Text("${current.toInt()} / ${goal.toInt()} $unit", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}
