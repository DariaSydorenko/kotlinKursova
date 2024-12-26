package com.example.bracesbuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bracesbuddy.ui.icons.Icons
import androidx.compose.material3.Icon
import com.example.bracesbuddy.ui.theme.Colors

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Головна", "home", Icons.Home()),
        BottomNavItem("Календар", "calendar", Icons.Calendar()),
        BottomNavItem("Фото", "album", Icons.Album()),
        BottomNavItem("Налаштування", "settings", Icons.Settings())
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    val isAuthScreen = currentRoute == "login" || currentRoute == "registration"

    if (!isAuthScreen) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Colors.NavBarColor)
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(
                            width = 1.dp,
                            color = Colors.NavBarSelectColor,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .background(
                            color = if (isSelected) Colors.NavBarSelectColor else Colors.NavBarColor,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.label,
                        tint = Colors.TitleColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.painter.Painter
)

