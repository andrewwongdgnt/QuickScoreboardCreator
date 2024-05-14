package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.R

enum class ScoreboardType(
    @RawRes val rawRes: Int? = null,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @StringRes val intervalLabelRes: Int,
) {
    NONE(titleRes = R.string.blank, descriptionRes = R.string.blank, intervalLabelRes = R.string.blank),
    BASKETBALL(R.raw.basketball, R.string.basketball, R.string.basketball_description, R.string.quarter),
    HOCKEY(R.raw.hockey, R.string.hockey, R.string.hockey_description, R.string.period),
    SPIKEBALL(R.raw.spikeball, R.string.spikeball, R.string.spikeball_description, R.string.game),
    TENNIS(R.raw.tennis, R.string.tennis, R.string.tennis_description, R.string.set),
}