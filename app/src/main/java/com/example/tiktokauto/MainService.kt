package com.example.tiktokauto

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class MainService : Service() {

    private lateinit var routineManager: ClickRoutineManager

    override fun onCreate() {
        super.onCreate()
        Log.d("MainService", "MainService 시작됨")

        startForegroundServiceWithNotification()

        routineManager = ClickRoutineManager(this) { point ->
            AutoClickAccessibility.sendClick(point)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MainService", "루틴 실행 명령 수신")
        routineManager.startRoutine()
        return START_STICKY
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "routine_service_channel"
        val channelName = "Auto Click Routine"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(chan)
        }

        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle("자동 루틴 실행 중")
            .setContentText("화면이 꺼져도 루틴이 계속 동작합니다.")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
