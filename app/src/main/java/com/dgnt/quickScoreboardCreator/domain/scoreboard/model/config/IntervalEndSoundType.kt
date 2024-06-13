package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.R

enum class IntervalEndSoundType(
    @RawRes val rawRes: Int? = null,
    @StringRes val titleRes: Int=R.string.blank
) {
    NONE,
    BELL(R.raw.bell, R.string.bell),
    BUZZER(R.raw.buzzer, R.string.buzzer),
    LOW_BUZZER(R.raw.low_buzzer, R.string.lowBuzzer),
    HORN(R.raw.horn, R.string.horn),
    WHISTLE(R.raw.whistle, R.string.whistle),
}