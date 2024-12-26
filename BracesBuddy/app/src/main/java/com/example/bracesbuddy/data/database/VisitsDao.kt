package com.example.bracesbuddy.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.bracesbuddy.data.model.Visits

@Dao
interface VisitsDao {
    @Query("SELECT * FROM visits WHERE userId = :userId")
    suspend fun getVisitsByUserId(userId: Int): List<Visits>

    @Query("SELECT * FROM visits WHERE userId = :userId AND visitDate = :date LIMIT 1")
    suspend fun getVisitByDate(userId: Int, date: String): Visits?

    @Query("SELECT EXISTS(SELECT 1 FROM visits WHERE userId = :userId AND visitDate = :date)")
    suspend fun hasVisit(userId: Int, date: String): Boolean

    @Query("INSERT INTO visits (userId, visitName, visitDate, visitTime) VALUES (:userId, :name, :date, :time)")
    suspend fun insertVisit(userId: Int, name: String, date: String, time: String)

    @Query("UPDATE visits SET visitName = :name, visitDate = :date, visitTime = :time WHERE id = :visitId")
    suspend fun updateVisit(visitId: Int, name: String, date: String, time: String)

    @Query("DELETE FROM visits WHERE id = :visitId")
    suspend fun deleteVisit(visitId: Int)

    @Query("SELECT * FROM visits WHERE userId = :userId AND visitDate LIKE :month || '%'")
    suspend fun getVisitsInMonth(userId: Int, month: String): List<Visits>
}