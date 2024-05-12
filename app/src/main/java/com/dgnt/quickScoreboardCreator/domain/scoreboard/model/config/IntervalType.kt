package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.R

enum class IntervalType(
    @StringRes val nameRes: Int,
) {
    PERIOD(R.string.period),
    QUARTER(R.string.quarter),
    GAME(R.string.game),
    SET(R.string.set),
    ROUND(R.string.round)
}