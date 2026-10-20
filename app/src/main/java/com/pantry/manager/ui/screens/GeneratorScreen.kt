package com.pantry.manager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pantry.manager.data.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorScreen() {
    var prioritizeExpiring by remember { mutableStateOf(true) }
    var allowMissingIngredients by remember { mutableStateOf(false) }
    var applyAllergyFilter by remember { mutableStateOf(true) }
    var customRequirement by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }

    var generatedRecipes by remember {
        mutableStateOf(
            listOf(
                Recipe(
                    id = 1,
                    title = "Creamy Chicken & Spinach Pasta",
                    instructions = "1. Cook chicken breast.\n2. Sauté spinach & tomatoes.\n3. Toss with milk & pasta.",
                    ingredientsNeeded = "Chicken Breast, Fresh Spinach, Whole Milk",
                    servingSize = "2 Servings",
                    isLiked = false,
                    caloriesPerServing = 480
                )
            )
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("AI Recipe Generator") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Recipe Generation Options", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Prioritize Expiring Ingredients")
                Switch(checked = prioritizeExpiring, onCheckedChange = { prioritizeExpiring = it })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Include Recipes with Missing Ingredients")
                Switch(checked = allowMissingIngredients, onCheckedChange = { allowMissingIngredients = it })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Filter by Saved Allergies / Dietary Rules")
                Switch(checked = applyAllergyFilter, onCheckedChange = { applyAllergyFilter = it })
            }

            OutlinedTextField(
                value = customRequirement,
                onValueChange = { customRequirement = it },
                label = { Text("Custom Requirements (e.g., No salt, Under 20 mins)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    isGenerating = true
                    val newRecipe = Recipe(
                        id = System.currentTimeMillis(),
                        title = "Spicy Tomato & Egg Scramble",
                        instructions = "1. Sauté canned tomatoes.\n2. Whisk eggs and scramble.\n3. Serve warm.",
                        ingredientsNeeded = "Canned Tomatoes, Eggs",
                        servingSize = "1 Serving",
                        isLiked = false,
                        caloriesPerServing = 250
                    )
                    generatedRecipes = listOf(newRecipe) + generatedRecipes
                    isGenerating = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isGenerating) "Generating Recipes..." else "Generate Recipe Ideas")
            }

            HorizontalDivider()

            Text("Suggested Recipes:", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(generatedRecipes, key = { it.id }) { recipe ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(recipe.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                IconButton(
                                    onClick = {
                                        generatedRecipes = generatedRecipes.map {
                                            if (it.id == recipe.id) it.copy(isLiked = !it.isLiked) else it
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (recipe.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Like Recipe",
                                        tint = if (recipe.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Text("Serving Size: ${recipe.servingSize} • Calories: ${recipe.caloriesPerServing ?: "N/A"}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Ingredients: ${recipe.ingredientsNeeded}", fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Instructions:\n${recipe.instructions}")
                        }
                    }
                }
            }
        }
    }
}
