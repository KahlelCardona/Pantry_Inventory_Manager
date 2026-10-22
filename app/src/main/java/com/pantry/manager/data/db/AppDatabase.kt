package com.pantry.manager.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pantry.manager.data.model.Allergy
import com.pantry.manager.data.model.Ingredient
import com.pantry.manager.data.model.Recipe
import com.pantry.manager.data.model.ShoppingList
import com.pantry.manager.data.model.ShoppingListItem

@Database(
    entities = [
        Ingredient::class,
        Recipe::class,
        Allergy::class,
        ShoppingList::class,
        ShoppingListItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pantry_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
