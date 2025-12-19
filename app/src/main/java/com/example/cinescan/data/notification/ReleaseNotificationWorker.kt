package com.example.cinescan.data.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

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
        // TODO: Implementar en tarea 5.3
    }

    companion object {
        const val KEY_ANALYSIS_ID = "analysis_id"
        const val KEY_TITLE = "title"
        const val KEY_PLATFORM = "platform"
        const val KEY_DAYS_UNTIL_RELEASE = "days_until_release"
    }
}
