package com.example.bracesbuddy.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bracesbuddy.data.model.Settings

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: Settings)

    @Query("SELECT * FROM settings WHERE userId = :userId LIMIT 1")
    suspend fun getSettingsByUserId(userId: Int): Settings?

    @Query("SELECT bracesDate FROM settings WHERE userId = :userId LIMIT 1")
    suspend fun getBracesDateByUserId(userId: Int): String?

    @Query("SELECT removalDate FROM settings WHERE userId = :userId LIMIT 1")
    suspend fun getRemovalDateByUserId(userId: Int): String?

    @Query("UPDATE settings SET bracesDate = :bracesDate WHERE userId = :userId")
    suspend fun updateBracesDate(userId: Int, bracesDate: String)

    @Query("UPDATE settings SET removalDate = :removalDate WHERE userId = :userId")
    suspend fun updateRemovalDate(userId: Int, removalDate: String)

    @Query("UPDATE settings SET doctorName = :doctorName WHERE userId = :userId")
    suspend fun updateDoctorName(userId: Int, doctorName: String)

    @Query("UPDATE settings SET clinicName = :clinicName WHERE userId = :userId")
    suspend fun updateClinicName(userId: Int, clinicName: String)

    @Query("UPDATE settings SET clinicPhone = :clinicPhone WHERE userId = :userId")
    suspend fun updateClinicPhone(userId: Int, clinicPhone: String)

//    @Query("SELECT cleaningReminderTimes FROM settings WHERE userId = :userId")
//    fun getCleaningReminderTimes(userId: Int): LiveData<String>

//    @Query("UPDATE settings SET cleaningRemindersCount = :cleaningRemindersCount WHERE userId = :userId")
//    suspend fun updateCleaningRemindersCount(userId: Int, cleaningRemindersCount: Int)
//
//    @Query("UPDATE settings SET cleaningReminderTimes = :cleaningReminderTimes WHERE userId = :userId")
//    suspend fun updateCleaningReminderTimes(userId: Int, cleaningReminderTimes: String)
//
//    @Query("UPDATE settings SET visitRemindersEnabled = :visitRemindersEnabled WHERE userId = :userId")
//    suspend fun updateVisitRemindersEnabled(userId: Int, visitRemindersEnabled: Boolean)
//
//    @Query("UPDATE settings SET visitReminderDaysBefore = :visitReminderDaysBefore WHERE userId = :userId")
//    suspend fun updateVisitReminderDaysBefore(userId: Int, visitReminderDaysBefore: String)
//
//    @Query("UPDATE settings SET photoRemindersEnabled = :photoRemindersEnabled WHERE userId = :userId")
//    suspend fun updatePhotoRemindersEnabled(userId: Int, photoRemindersEnabled: Boolean)
//
//    @Query("UPDATE settings SET photoReminderFrequency = :photoReminderFrequency WHERE userId = :userId")
//    suspend fun updatePhotoReminderFrequency(userId: Int, photoReminderFrequency: String)
}