package com.example.bracesbuddy.utils

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Data
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleCleaningReminders(context: Context, reminderTimes: List<String>) {
    val workManager = WorkManager.getInstance(context)

    reminderTimes.forEach { time ->
        val (hour, minute) = time.split(":").map { it.toInt() }

        val delayMillis = calculateDelayForReminder(hour, minute)

        val data = Data.Builder()
            .putString("time", time)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        workManager.enqueue(workRequest)
    }
}

fun calculateDelayForReminder(hour: Int, minute: Int): Long {
    val now = Calendar.getInstance()
    val reminderTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }

    return if (reminderTime.before(now)) {
        reminderTime.add(Calendar.DAY_OF_YEAR, 1)
        reminderTime.timeInMillis - now.timeInMillis
    } else {
        reminderTime.timeInMillis - now.timeInMillis
    }
}