package com.example.cinescan.data.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cinescan.MainActivity

class ReleaseNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val analysisId = inputData.getLong(KEY_ANALYSIS_ID, -1)
        val title = inputData.getString(KEY_TITLE) ?: return Result.failure()
        val platform = inputData.getString(KEY_PLATFORM) ?: ""
        val daysUntilRelease = inputData.getInt(KEY_DAYS_UNTIL_RELEASE, -1)

        if (analysisId == -1L || daysUntilRelease == -1) {
            return Result.failure()
        }

        showNotification(
            analysisId = analysisId,
            title = title,
            platform = platform,
            daysUntilRelease = daysUntilRelease
        )

        return Result.success()
    }

    private fun showNotification(
        analysisId: Long,
        title: String,
        platform: String,
        daysUntilRelease: Int
    ) {
        NotificationHelper.createNotificationChannel(applicationContext)

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MainActivity.EXTRA_ANALYSIS_ID, analysisId)
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            analysisId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val message = NotificationHelper.getNotificationMessage(daysUntilRelease, platform)
        val notification = NotificationHelper.buildNotification(
            context = applicationContext,
            title = title,
            message = message,
            pendingIntent = pendingIntent
        )

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(analysisId.toInt(), notification)
    }

    companion object {
        const val KEY_ANALYSIS_ID = "analysis_id"
        const val KEY_TITLE = "title"
        const val KEY_PLATFORM = "platform"
        const val KEY_DAYS_UNTIL_RELEASE = "days_until_release"
    }
}
