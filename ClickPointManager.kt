package com.example.tiktokauto

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

object ClickPointManager {

    private const val PREF_NAME = "click_points"
    private const val KEY_POINTS = "points"

    fun saveClickPoints(context: Context, points: List<ClickPoint>) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val jsonArray = JSONArray()
        for (point in points) {
            val obj = JSONObject()
            obj.put("name", point.name)
            obj.put("x", point.x)
            obj.put("y", point.y)
            jsonArray.put(obj)
        }

        editor.putString(KEY_POINTS, jsonArray.toString())
        editor.apply()
    }

    fun loadClickPoints(context: Context): List<ClickPoint> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_POINTS, null) ?: return getDefaultClickPoints()

        val result = mutableListOf<ClickPoint>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                result.add(
                    ClickPoint(
                        name = obj.getString("name"),
                        x = obj.getInt("x"),
                        y = obj.getInt("y")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return getDefaultClickPoints()
        }

        return result
    }

    private fun getDefaultClickPoints(): List<ClickPoint> {
        return listOf(
            ClickPoint("first_click", 360, 900),
            ClickPoint("popup_confirm", 360, 1260),
            ClickPoint("popup_close", 360, 1380),
            ClickPoint("main_click", 360, 1020)
        )
    }
}
