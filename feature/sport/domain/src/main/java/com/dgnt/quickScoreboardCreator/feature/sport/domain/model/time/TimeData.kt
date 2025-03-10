package com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time

import java.util.Locale

data class TimeData(
    val minute: Int,
    val second: Int,
    val centiSecond: Int,
) {
    fun formatTime(forceMin: Boolean = false): String {
        return if (minute == 0 && !forceMin)
            String.format(Locale.ROOT, "%d.%d", second, centiSecond)
        else
            String.format(Locale.ROOT, "%02d:%02d", minute, second)
    }
}