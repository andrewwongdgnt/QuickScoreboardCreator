package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalEndSound

enum class IntervalEndSoundType {
    NONE,
    BELL,
    BUZZER,
    LOW_BUZZER,
    HORN,
    WHISTLE;

    fun toIntervalEndSound() =
        when (this) {
            NONE -> IntervalEndSound.None
            BELL -> IntervalEndSound.Bell
            BUZZER -> IntervalEndSound.Buzzer
            LOW_BUZZER -> IntervalEndSound.LowBuzzer
            HORN -> IntervalEndSound.Horn
            WHISTLE -> IntervalEndSound.Whistle
        }
}