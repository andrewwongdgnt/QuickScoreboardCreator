package com.dgnt.quickScoreboardCreator.core.data.sport.filedto

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.interval.IntervalEndSound


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