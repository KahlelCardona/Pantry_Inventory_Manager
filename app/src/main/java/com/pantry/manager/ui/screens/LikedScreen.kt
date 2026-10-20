package com.pantry.manager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
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
fun LikedScreen() {
    var likedRecipes by remember {
        mutableStateOf(
            listOf(
                Recipe(
                    id = 101,
                    title = "Hearty Spinach & Tomato Omelet",
                    instructions = "Whisk eggs with chopped spinach and canned tomatoes. Cook on medium heat.",
                    ingredientsNeeded = "Eggs, Fresh Spinach, Canned Tomatoes",
                    servingSize = "2 Servings",
                    isLiked = true,
                    caloriesPerServing = 310
                )
            )
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Liked Recipes") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (likedRecipes.isEmpty()) {
                Text(
                    text = "No liked recipes yet. Generate and save recipes from the AI Recipe Generator!",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(likedRecipes, key = { it.id }) { recipe ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(recipe.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Serving Size: ${recipe.servingSize} • Calories: ${recipe.caloriesPerServing ?: "N/A"}")
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Ingredients: ${recipe.ingredientsNeeded}", fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(recipe.instructions)
                                }
                                IconButton(
                                    onClick = { likedRecipes = likedRecipes.filter { it.id != recipe.id } }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove from Liked")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
