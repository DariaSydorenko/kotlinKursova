package com.example.bracesbuddy.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.bracesbuddy.data.model.Photos
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {
    @Query("SELECT * FROM photos WHERE userId = :userId ORDER BY photoDate DESC")
    fun getAllPhotos(userId: Int): Flow<List<Photos>>

    @Query("INSERT INTO photos (userId, photoUri, photoDate) VALUES (:userId, :path, :date)")
    suspend fun insertPhoto(userId: Int, path: String, date: String)
}