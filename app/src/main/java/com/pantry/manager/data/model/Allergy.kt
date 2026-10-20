package com.pantry.manager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allergies")
data class Allergy(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val allergyName: String,
    val isEnabled: Boolean = true
)
