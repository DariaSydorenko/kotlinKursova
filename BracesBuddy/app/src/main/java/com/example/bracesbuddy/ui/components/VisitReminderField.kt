package com.example.bracesbuddy.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun VisitReminderField(
    enabled: Boolean,
    daysBefore: Int,
    onDaysBeforeChange: (Int) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("Візити", style = MaterialTheme.typography.bodyLarge)
            if (enabled) {
                Text("Нагадати про візит за:")
            }
        }
        Checkbox(checked = enabled, onCheckedChange = onCheckedChange)
    }
}
