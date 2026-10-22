package com.pantry.manager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pantry.manager.data.db.AppDatabase
import com.pantry.manager.data.model.Ingredient
import com.pantry.manager.data.repository.IngredientRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: IngredientRepository

    val ingredients: StateFlow<List<Ingredient>>

    init {
        val ingredientDao = AppDatabase.getDatabase(application).ingredientDao()
        repository = IngredientRepository(ingredientDao)
        ingredients = repository.allIngredients.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        viewModelScope.launch {
            repository.prepopulateIfEmpty()
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.addIngredient(ingredient)
        }
    }

    fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.deleteIngredient(ingredient)
        }
    }
}
