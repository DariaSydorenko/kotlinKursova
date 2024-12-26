package com.example.bracesbuddy.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bracesbuddy.ui.theme.Colors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DateInputField(
    date: String,
    onDateSelected: (String) -> Unit,
    placeholder: String
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(Colors.InputFieldBackground, RoundedCornerShape(20.dp))
            .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp))
            .clickable {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(
                            Calendar.getInstance().apply {
                                set(year, month, day)
                            }.time
                        )
                        onDateSelected(formattedDate)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            .padding(16.dp)
    ) {
        Text(
            text = if (date.isNotEmpty()) date else placeholder,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Colors.TitleColor,
                textAlign = TextAlign.Center
            )
        )
    }
}