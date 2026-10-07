package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class RideTrackingService : Service() {

    companion object {
        const val CHANNEL_ID = "groupby_ride_channel"
        const val NOTIFICATION_ID = 101
        const val ACTION_START = "ACTION_START_RIDE_TRACKING"
        const val ACTION_STOP = "ACTION_STOP_RIDE_TRACKING"
        const val EXTRA_ROUTE_NAME = "EXTRA_ROUTE_NAME"
        const val EXTRA_SPEED = "EXTRA_SPEED"

        fun startTracking(context: Context, routeName: String = "Mulholland Loop", speedMph: Int = 62) {
            val intent = Intent(context, RideTrackingService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_ROUTE_NAME, routeName)
                putExtra(EXTRA_SPEED, speedMph)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopTracking(context: Context) {
            val intent = Intent(context, RideTrackingService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }

        val routeName = intent?.getStringExtra(EXTRA_ROUTE_NAME) ?: "Mulholland Loop"
        val speedMph = intent?.getIntExtra(EXTRA_SPEED, 64) ?: 64

        val notification = buildNotification(routeName, speedMph)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "GroupBy Active Ride Telemetry",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Continuous GPS telemetry and background group sync during active rides"
                setShowBadge(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(routeName: String, speed: Int): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GroupBy Ride Active: $routeName")
            .setContentText("Telemetry active · $speed mph · 3 riders connected")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
}
