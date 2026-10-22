package com.pantry.manager.data.db

import androidx.room.TypeConverter
import com.pantry.manager.data.model.StorageLocation

class Converters {
    @TypeConverter
    fun fromStorageLocation(location: StorageLocation?): String? {
        return location?.name
    }

    @TypeConverter
    fun toStorageLocation(value: String?): StorageLocation? {
        return value?.let { StorageLocation.valueOf(it) }
    }
}
