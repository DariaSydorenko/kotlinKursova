package com.example.bracesbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.data.database.DatabaseHelp
import com.example.bracesbuddy.navigation.Navigation
import com.example.bracesbuddy.ui.components.BottomNavBar
import com.example.bracesbuddy.ui.theme.BracesBuddyTheme
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)

//        DatabaseHelp.deleteDatabase(applicationContext)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "braces_buddy_db"
        ).build()

        setContent {
            BracesBuddyTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    Navigation(
                        navController = navController,
                        db = db,
                        modifier = Modifier.padding(innerPadding),
                        context = applicationContext
                    )
                }
            }
        }
    }
}
