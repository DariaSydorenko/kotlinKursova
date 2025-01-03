package com.example.bracesbuddy.utils

import java.text.SimpleDateFormat
import java.util.*

fun parseDate(dateString: String): Date? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.parse(dateString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return try {
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        e.printStackTrace()
        date
    }
}