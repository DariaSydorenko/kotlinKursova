package com.example.bracesbuddy.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.ui.screens.*
import com.example.bracesbuddy.ui.screens.auth.LoginScreen
import com.example.bracesbuddy.ui.screens.auth.RegistrationScreen

@Composable
fun Navigation(navController: NavHostController, db: AppDatabase, modifier: Modifier = Modifier, context: Context) {
    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable("home") { HomeScreen(db) }
        composable("calendar") { CalendarScreen(db) }
        composable("album") { AlbumScreen(db) }
        composable("settings") { SettingsScreen(db) }
        composable("login") { LoginScreen(navController, db, context) }
        composable("registration") { RegistrationScreen(navController, db) }
    }
}

