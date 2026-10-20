package com.pantry.manager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val instructions: String,
    val ingredientsNeeded: String,
    val servingSize: String,
    val isLiked: Boolean = false,
    val caloriesPerServing: Int? = null
)
