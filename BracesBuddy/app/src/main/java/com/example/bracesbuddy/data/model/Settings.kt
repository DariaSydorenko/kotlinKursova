package com.example.bracesbuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val bracesDate: String,
    val removalDate: String,
    val doctorName: String,
    val clinicName: String,
    val clinicPhone: String,
//    val cleaningRemindersCount: Int,
//    val cleaningReminderTimes: String,
//    val visitRemindersEnabled: Boolean,
//    val visitReminderDaysBefore: String,
//    val photoRemindersEnabled: Boolean,
//    val photoReminderFrequency: String
)
