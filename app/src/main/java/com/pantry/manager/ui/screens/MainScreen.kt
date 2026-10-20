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

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Pantry : Screen("pantry", "Pantry", Icons.Default.Home)
    data object Generator : Screen("generator", "AI Recipes", Icons.Default.AutoAwesome)
    data object Scanner : Screen("scanner", "Scanner", Icons.Default.QrCodeScanner)
    data object Liked : Screen("liked", "Liked", Icons.Default.Favorite)
    data object Nutrition : Screen("nutrition", "Nutrition", Icons.Default.PieChart)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun MainScreen() {
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
                Screen.Pantry -> HomeScreen(onNavigateToScanner = { currentScreen = Screen.Scanner })
                Screen.Generator -> GeneratorScreen()
                Screen.Scanner -> BarcodeScreen()
                Screen.Liked -> LikedScreen()
                Screen.Nutrition -> NutritionScreen()
                Screen.Settings -> AllergiesScreen()
            }
        }
    }
}
