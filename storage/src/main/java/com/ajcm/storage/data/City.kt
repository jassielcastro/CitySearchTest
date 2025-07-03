package com.ajcm.storage.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = City.TABLE_NAME)
data class City(
    @PrimaryKey
    val id: Int,
    val country: String,
    val name: String,
    @Embedded
    val coordinate: Coordinate,
    val favorite: Boolean
) {
    companion object {
        const val TABLE_NAME = "cities"
    }
}
