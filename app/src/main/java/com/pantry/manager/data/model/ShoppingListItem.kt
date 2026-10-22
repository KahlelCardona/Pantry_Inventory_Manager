package com.pantry.manager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "ShoppingListItem",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingList::class,
            parentColumns = ["id"],
            childColumns = ["shoppingListID"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("shoppingListID")]
)
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shoppingListID: Long,
    val name: String,
    val quantity: Double,
    val gotten: Boolean = false
)