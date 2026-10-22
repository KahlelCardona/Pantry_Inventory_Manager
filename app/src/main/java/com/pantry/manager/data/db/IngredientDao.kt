package com.pantry.manager.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pantry.manager.data.model.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients ORDER BY expirationDateMillis ASC")
    fun getAllIngredients(): Flow<List<Ingredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: Ingredient): Long

    @Delete
    fun deleteIngredient(ingredient: Ingredient): Int

    @Query("DELETE FROM ingredients WHERE id = :id")
    fun deleteIngredientById(id: Long): Int

    @Query("SELECT COUNT(*) FROM ingredients")
    fun getIngredientCount(): Int
}
