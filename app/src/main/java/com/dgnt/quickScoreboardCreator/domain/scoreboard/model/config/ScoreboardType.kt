package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.R

enum class ScoreboardType(
    @RawRes val rawRes: Int? = null,
    @StringRes val titleRes: Int=R.string.blank,
    @StringRes val descriptionRes: Int=R.string.blank,
    @StringRes val intervalLabelRes: Int=R.string.blank,
    @StringRes val secondaryScoreLabelRes: Int=R.string.blank,
) {
    NONE(),
    BASKETBALL(R.raw.basketball, R.string.basketball, R.string.basketball_description, R.string.quarter, R.string.fouls),
    HOCKEY(R.raw.hockey, R.string.hockey, R.string.hockey_description, R.string.period, R.string.penalties),
    SPIKEBALL(R.raw.spikeball, R.string.spikeball, R.string.spikeball_description, R.string.game),
    TENNIS(R.raw.tennis, R.string.tennis, R.string.tennis_description, R.string.game),
}