package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.R

enum class ScoreboardType(
    @RawRes val rawRes: Int? = null,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
) {
    NONE(titleRes = R.string.blank, descriptionRes = R.string.blank),
    BASKETBALL(R.raw.basketball, R.string.basketball, R.string.basketball_description),
    HOCKEY(R.raw.hockey, R.string.hockey, R.string.hockey_description),
    SPIKEBALL(R.raw.spikeball, R.string.spikeball, R.string.spikeball_description)
}