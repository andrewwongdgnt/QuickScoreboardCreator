package com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval

sealed class IntervalEndSound {
    data object None : IntervalEndSound()
    data object Bell : IntervalEndSound()
    data object Buzzer : IntervalEndSound()
    data object LowBuzzer : IntervalEndSound()
    data object Horn : IntervalEndSound()
    data object Whistle : IntervalEndSound()
}