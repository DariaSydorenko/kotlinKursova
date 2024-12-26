package com.example.bracesbuddy.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhotoReminderField(
    enabled: Boolean,
    frequency: String,
    onFrequencyChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("Фотографії", style = MaterialTheme.typography.bodyLarge)
            if (enabled) {
                Text("Раз в:")
                // Add Dropdown for frequency selection
            }
        }
        Checkbox(checked = enabled, onCheckedChange = onCheckedChange)
    }
}
