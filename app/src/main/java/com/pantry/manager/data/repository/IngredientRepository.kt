package com.pantry.manager.data.repository

import com.pantry.manager.data.db.IngredientDao
import com.pantry.manager.data.model.Ingredient
import com.pantry.manager.data.model.StorageLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class IngredientRepository(private val ingredientDao: IngredientDao) {
    val allIngredients: Flow<List<Ingredient>> = ingredientDao.getAllIngredients()

    suspend fun addIngredient(ingredient: Ingredient): Long = withContext(Dispatchers.IO) {
        ingredientDao.insertIngredient(ingredient)
    }

    suspend fun deleteIngredient(ingredient: Ingredient): Int = withContext(Dispatchers.IO) {
        ingredientDao.deleteIngredient(ingredient)
    }

    suspend fun deleteIngredientById(id: Long): Int = withContext(Dispatchers.IO) {
        ingredientDao.deleteIngredientById(id)
    }

    suspend fun prepopulateIfEmpty() = withContext(Dispatchers.IO) {
        if (ingredientDao.getIngredientCount() == 0) {
            val initialIngredients = listOf(
                Ingredient(0, "Whole Milk", "1 Gallon", StorageLocation.FRIDGE, System.currentTimeMillis() + 86400000L * 2, "12345678", 150, 8f, 12f, 8f),
                Ingredient(0, "Chicken Breast", "2 lbs", StorageLocation.FREEZER, System.currentTimeMillis() + 86400000L * 14, "87654321", 165, 31f, 0f, 3.6f),
                Ingredient(0, "Fresh Spinach", "1 Bag", StorageLocation.FRIDGE, System.currentTimeMillis() + 86400000L * 1, "11223344", 23, 2.9f, 3.6f, 0.4f),
                Ingredient(0, "Canned Tomatoes", "3 Cans", StorageLocation.PANTRY, System.currentTimeMillis() + 86400000L * 180, "44332211", 50, 1f, 10f, 0f)
            )
            initialIngredients.forEach { ingredientDao.insertIngredient(it) }
        }
    }
}
