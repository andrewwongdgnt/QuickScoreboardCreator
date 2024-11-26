package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.R as R2

enum class ScoreboardType(
    @RawRes val rawRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val icon: ScoreboardIcon,
    @StringRes val intervalLabelRes: Int,
    @StringRes val secondaryScoreLabelRes: Int = R.string.blank,
) {
    BASKETBALL(R2.raw.basketball, R.string.basketball, R.string.basketball_description, ScoreboardIcon.BASKETBALL, R.string.quarter, R.string.fouls),
    HOCKEY(R2.raw.hockey, R.string.hockey, R.string.hockey_description, ScoreboardIcon.HOCKEY, R.string.period, R.string.penalties),
    SPIKEBALL(R2.raw.spikeball, R.string.spikeball, R.string.spikeball_description, ScoreboardIcon.SPIKEBALL, R.string.game),
    TENNIS(R2.raw.tennis, R.string.tennis, R.string.tennis_description, ScoreboardIcon.TENNIS, R.string.game),
    BOXING(R2.raw.boxing, R.string.boxing, R.string.boxing_description, ScoreboardIcon.BOXING, R.string.round),
}