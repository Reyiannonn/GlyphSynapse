package com.glyphsynapse.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Preset(
    val id: String,
    val name: String,
    val animationName: String,
    val speedMultiplier: Float,
    val brightness: Float,
    val stealthMode: Boolean
)

@Serializable
data class ScheduleWindow(
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int
) {
    fun isActiveNow(): Boolean {
        val cal = java.util.Calendar.getInstance()
        val nowMinutes = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
        val startMinutes = startHour * 60 + startMinute
        val endMinutes = endHour * 60 + endMinute
        return if (startMinutes <= endMinutes) {
            nowMinutes in startMinutes..endMinutes
        } else {
            nowMinutes >= startMinutes || nowMinutes <= endMinutes
        }
    }
}
