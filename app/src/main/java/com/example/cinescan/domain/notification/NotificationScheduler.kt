package com.example.cinescan.domain.notification

interface NotificationScheduler {
    
    fun scheduleReleaseNotifications(
        analysisId: Long,
        title: String,
        platform: String,
        releaseTimestamp: Long
    )
    
    fun cancelNotifications(analysisId: Long)
}
