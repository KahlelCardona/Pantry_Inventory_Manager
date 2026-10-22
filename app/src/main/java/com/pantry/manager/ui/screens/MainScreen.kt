package com.pantry.manager.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.lifecycle.viewmodel.compose.viewModel
import com.pantry.manager.data.model.Ingredient
import com.pantry.manager.data.model.StorageLocation
import com.pantry.manager.ui.viewmodel.HomeScreenViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Pantry : Screen("pantry", "Pantry", Icons.Default.Home)
    data object Generator : Screen("generator", "AI Recipes", Icons.Default.AutoAwesome)
    data object Scanner : Screen("scanner", "Scanner", Icons.Default.QrCodeScanner)
    data object Liked : Screen("liked", "Liked", Icons.Default.Favorite)
    data object Nutrition : Screen("nutrition", "Nutrition", Icons.Default.PieChart)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun MainScreen(
    viewModel: HomeScreenViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Scanner) }

    val bottomNavItems = listOf(
        Screen.Pantry,
        Screen.Generator,
        Screen.Scanner,
        Screen.Liked,
        Screen.Nutrition,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentScreen.route == screen.route,
                        onClick = { currentScreen = screen }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.Pantry -> HomeScreen(
                    onNavigateToScanner = { currentScreen = Screen.Scanner },
                    viewModel = viewModel
                )
                Screen.Generator -> GeneratorScreen()
                Screen.Scanner -> BarcodeScreen(
                    onItemScanned = { name, barcode ->
                        viewModel.addIngredient(
                            Ingredient(
                                name = name,
                                quantity = "1 unit",
                                storageLocation = StorageLocation.PANTRY,
                                expirationDateMillis = System.currentTimeMillis() + (86400000L * 7),
                                barcode = barcode
                            )
                        )
                    }
                )
                Screen.Liked -> LikedScreen()
                Screen.Nutrition -> NutritionScreen()
                Screen.Settings -> AllergiesScreen()
            }
        }
    }
}
