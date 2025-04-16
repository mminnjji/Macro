package com.example.tiktokauto

import android.content.Context
import android.os.PowerManager
import android.util.Log

object WakeLockUtil {

    private var wakeLock: PowerManager.WakeLock? = null

    fun wakeUpScreen(context: Context) {
        try {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }

            wakeLock = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "TikTokAuto:WakeLock"
            )
            wakeLock?.acquire(5000)  // 5초만 유지

            Log.d("WakeLock", "화면 깨우기 수행됨")
        } catch (e: Exception) {
            Log.e("WakeLock", "WakeLock 실패: ${e.message}")
        }
    }
}
