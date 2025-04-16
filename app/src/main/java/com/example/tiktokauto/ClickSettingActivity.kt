package com.example.tiktokauto

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ClickSettingActivity : AppCompatActivity() {

    private val clickPoints = mutableListOf<ClickPoint>()
    private val stepNames = listOf(
        "first_click",
        "popup_confirm",
        "popup_close",
        "main_click"
    )
    private var currentStep = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val instruction = TextView(this).apply {
            text = "화면을 터치해서 [${stepNames[currentStep]}] 좌표를 지정하세요"
            textSize = 20f
            setPadding(20, 200, 20, 20)
        }

        setContentView(instruction)

        instruction.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val x = event.x.toInt()
                val y = event.y.toInt()
                val name = stepNames[currentStep]
                clickPoints.add(ClickPoint(name, x, y))
                Toast.makeText(this, "[$name] 좌표 저장됨: ($x, $y)", Toast.LENGTH_SHORT).show()

                currentStep++
                if (currentStep >= stepNames.size) {
                    // 저장 완료
                    ClickPointManager.saveClickPoints(this, clickPoints)
                    Toast.makeText(this, "모든 좌표 저장 완료!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    instruction.text = "다음: [${stepNames[currentStep]}] 좌표를 지정하세요"
                }
            }
            true
        }
    }
}
