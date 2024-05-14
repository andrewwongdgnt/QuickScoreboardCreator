package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time

import java.util.Locale

data class TimeData(
    val minute: Int,
    val second: Int,
    val centiSecond: Int,
) {
    fun formatTime(): String {
        return if (minute == 0)
            String.format(Locale.ROOT, "%d.%d", second, centiSecond)
        else
            String.format(Locale.ROOT, "%02d:%02d", minute, second)
    }
}