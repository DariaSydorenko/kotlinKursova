package com.example.bracesbuddy.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val time = inputData.getString("time") ?: return Result.failure()
        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.sendReminderNotification(time)
        return Result.success()
    }
}
