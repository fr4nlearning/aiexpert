package com.example.cinescan.data.notification

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cinescan.domain.notification.NotificationScheduler
import com.example.cinescan.domain.usecase.NotificationWindowCalculator
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerNotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationScheduler {

    private val workManager = WorkManager.getInstance(context)

    override fun scheduleReleaseNotifications(
        analysisId: Long,
        title: String,
        platform: String,
        releaseTimestamp: Long
    ) {
        val applicableWindows = NotificationWindowCalculator.calculateWindows(releaseTimestamp)
        
        if (applicableWindows.isEmpty()) {
            return
        }

        val currentTime = System.currentTimeMillis()

        applicableWindows.forEach { daysBeforeRelease ->
            val notificationTime = releaseTimestamp - (daysBeforeRelease * 24 * 60 * 60 * 1000L)
            val delay = notificationTime - currentTime

            if (delay > 0) {
                val inputData = Data.Builder()
                    .putLong(ReleaseNotificationWorker.KEY_ANALYSIS_ID, analysisId)
                    .putString(ReleaseNotificationWorker.KEY_TITLE, title)
                    .putString(ReleaseNotificationWorker.KEY_PLATFORM, platform)
                    .putInt(ReleaseNotificationWorker.KEY_DAYS_UNTIL_RELEASE, daysBeforeRelease)
                    .build()

                val workRequest = OneTimeWorkRequestBuilder<ReleaseNotificationWorker>()
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .addTag(getNotificationTag(analysisId))
                    .addTag(getWindowTag(analysisId, daysBeforeRelease))
                    .build()

                workManager.enqueue(workRequest)
            }
        }
    }

    override fun cancelNotifications(analysisId: Long) {
        workManager.cancelAllWorkByTag(getNotificationTag(analysisId))
    }

    private fun getNotificationTag(analysisId: Long): String {
        return "notification_analysis_$analysisId"
    }

    private fun getWindowTag(analysisId: Long, daysBeforeRelease: Int): String {
        return "notification_analysis_${analysisId}_window_$daysBeforeRelease"
    }
}
