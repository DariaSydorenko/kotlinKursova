package com.example.bracesbuddy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bracesbuddy.data.model.Photos
import com.example.bracesbuddy.data.model.User
import com.example.bracesbuddy.data.model.Settings
import com.example.bracesbuddy.data.model.Visits

@Database(
    entities = [User::class, Settings::class, Visits::class, Photos::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun settingsDao(): SettingsDao
    abstract fun visitsDao(): VisitsDao
    abstract fun photosDao(): PhotosDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "braces_buddy_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}