package com.example.cinescan.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.cinescan.R

object NotificationHelper {

    const val CHANNEL_ID = "release_notifications"
    const val CHANNEL_NAME = "Notificaciones de Estrenos"
    const val CHANNEL_DESCRIPTION = "Notificaciones sobre próximos estrenos de películas y series"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun buildNotification(
        context: Context,
        title: String,
        message: String,
        pendingIntent: android.app.PendingIntent
    ): android.app.Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    fun getNotificationMessage(daysUntilRelease: Int, platform: String): String {
        return when (daysUntilRelease) {
            0 -> "¡Hoy es el estreno${if (platform.isNotEmpty()) " en $platform" else ""}!"
            1 -> "Mañana es el estreno${if (platform.isNotEmpty()) " en $platform" else ""}"
            else -> "Faltan $daysUntilRelease días para el estreno${if (platform.isNotEmpty()) " en $platform" else ""}"
        }
    }
}
