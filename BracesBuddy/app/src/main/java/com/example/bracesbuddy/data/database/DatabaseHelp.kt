package com.example.bracesbuddy.data.database

import android.content.Context
import androidx.room.Room

object DatabaseHelp {

    fun deleteDatabase(context: Context) {
        context.deleteDatabase("braces_buddy_db")
    }

    fun createNewDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "braces_buddy_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}