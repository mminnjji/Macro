package com.example.tiktokauto

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AutoClickAccessibility : AccessibilityService() {

    companion object {
        private var instance: AutoClickAccessibility? = null

        fun sendClick(point: ClickPoint) {
            instance?.performClick(point)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d("Accessibility", "접근성 서비스 연결됨")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 사용하지 않음
    }

    override fun onInterrupt() {
        Log.d("Accessibility", "접근성 서비스 중단됨")
    }

    private fun performClick(point: ClickPoint) {
        val path = Path().apply {
            moveTo(point.x.toFloat(), point.y.toFloat())
        }

        val gesture = GestureDescription.Builder()
            .addStroke(
                GestureDescription.StrokeDescription(
                    path,
                    0L,    // delay
                    50L    // duration of press
                )
            ).build()

        val dispatched = dispatchGesture(gesture, null, null)

        Log.d("Accessibility", "클릭 전송됨: ${point.name} (${point.x}, ${point.y}) -> $dispatched")
    }
}
