package com.example.bracesbuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photos(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val photoUri: String,
    val photoDate: String
)
