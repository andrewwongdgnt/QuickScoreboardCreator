package com.dgnt.quickScoreboardCreator.domain.model.config

import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.R

enum class ScoreboardType(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
) {
    NONE(R.string.blank, R.string.blank),
    BASKETBALL(R.string.basketball, R.string.basketball_description),
    HOCKEY(R.string.hockey, R.string.hockey_description),
    SPIKEBALL(R.string.spikeball, R.string.spikeball_description)
}