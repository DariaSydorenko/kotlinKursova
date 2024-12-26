package com.example.bracesbuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visits")
data class Visits(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val visitName: String,
    val visitDate: String,
    val visitTime: String
)
