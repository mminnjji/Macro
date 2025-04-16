package com.example.tiktokauto

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.tiktokauto.utils.GestureUtil

class ClickRoutineManager(private val context: Context, private val gestureSender: (ClickPoint) -> Unit) {

    private val clickPoints by lazy { ClickPointManager.loadClickPoints(context) }
    private val handler = Handler(Looper.getMainLooper())

    fun startRoutine() {
        Log.d("Routine", "루틴 시작")

        // 1. TikTok 앱 실행
        AppLauncherUtil.launchTikTokLite(context)

        // 2. 순차 클릭 실행 (약간의 딜레이 넣기)
        handler.postDelayed({
            clickByName("first_click")
        }, 2000)

        handler.postDelayed({
            clickByName("popup_confirm")
        }, 3000)

        handler.postDelayed({
            clickByName("popup_close")
        }, 3500)

        handler.postDelayed({
            clickMainClick(5000)
        }, 4000)
    }

    private fun clickByName(name: String) {
        val point = clickPoints.find { it.name == name }
        if (point != null) {
            Log.d("Routine", "[$name] 클릭: (${point.x}, ${point.y})")
            gestureSender(point)
        } else {
            Log.w("Routine", "[$name] 좌표를 찾을 수 없음")
        }
    }

    private fun clickMainClick(count: Int) {
        val point = clickPoints.find { it.name == "main_click" }
        if (point == null) {
            Log.w("Routine", "[main_click] 좌표가 없음")
            return
        }

        repeatClick(point, count, 5)
    }

    private fun repeatClick(point: ClickPoint, count: Int, intervalMs: Long) {
        var remaining = count
        val clickRunnable = object : Runnable {
            override fun run() {
                if (remaining <= 0) return
                gestureSender(point)
                remaining--
                handler.postDelayed(this, intervalMs)
            }
        }
        handler.post(clickRunnable)
    }
}
