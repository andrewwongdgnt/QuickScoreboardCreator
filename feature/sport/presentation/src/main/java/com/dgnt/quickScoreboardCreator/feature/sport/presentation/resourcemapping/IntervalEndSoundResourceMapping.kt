package com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping

import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound

fun IntervalEndSound.titleRes() = when (this) {
    IntervalEndSound.Bell -> R.string.bell
    IntervalEndSound.Buzzer -> R.string.buzzer
    IntervalEndSound.Horn -> R.string.horn
    IntervalEndSound.LowBuzzer -> R.string.lowBuzzer
    IntervalEndSound.None -> R.string.none
    IntervalEndSound.Whistle -> R.string.whistle
}

fun IntervalEndSound.soundEffectRes() = when (this) {
    IntervalEndSound.Bell -> R.raw.bell
    IntervalEndSound.Buzzer -> R.raw.buzzer
    IntervalEndSound.Horn -> R.raw.horn
    IntervalEndSound.LowBuzzer -> R.raw.low_buzzer
    IntervalEndSound.None -> null
    IntervalEndSound.Whistle -> R.raw.whistle
}