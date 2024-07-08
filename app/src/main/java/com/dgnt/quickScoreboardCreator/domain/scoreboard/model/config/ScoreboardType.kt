package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon

enum class ScoreboardType(
    @RawRes val rawRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int ,
    val icon: ScoreboardIcon,
    @StringRes val intervalLabelRes: Int ,
    @StringRes val secondaryScoreLabelRes: Int = R.string.blank,
) {
    BASKETBALL(R.raw.basketball, R.string.basketball, R.string.basketball_description, ScoreboardIcon.BASKETBALL, R.string.quarter, R.string.fouls),
    HOCKEY(R.raw.hockey, R.string.hockey, R.string.hockey_description, ScoreboardIcon.HOCKEY, R.string.period, R.string.penalties),
    SPIKEBALL(R.raw.spikeball, R.string.spikeball, R.string.spikeball_description, ScoreboardIcon.BASKETBALL, R.string.game),
    TENNIS(R.raw.tennis, R.string.tennis, R.string.tennis_description, ScoreboardIcon.TENNIS, R.string.game),
}