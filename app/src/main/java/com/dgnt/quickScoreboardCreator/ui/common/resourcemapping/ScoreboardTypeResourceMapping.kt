package com.dgnt.quickScoreboardCreator.ui.common.resourcemapping

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardType
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.R as R2

fun ScoreboardType.rawRes() = when (this) {
    ScoreboardType.BASKETBALL -> R2.raw.basketball
    ScoreboardType.BOXING -> R2.raw.boxing
    ScoreboardType.HOCKEY -> R2.raw.hockey
    ScoreboardType.TENNIS -> R2.raw.tennis
    ScoreboardType.SPIKEBALL -> R2.raw.spikeball
}

fun ScoreboardType.titleRes() = when(this) {
    ScoreboardType.BASKETBALL -> R.string.basketball
    ScoreboardType.HOCKEY -> R.string.hockey
    ScoreboardType.SPIKEBALL -> R.string.spikeball
    ScoreboardType.TENNIS -> R.string.tennis
    ScoreboardType.BOXING -> R.string.boxing
}

fun ScoreboardType.descriptionRes() = when(this) {
    ScoreboardType.BASKETBALL -> R.string.basketball_description
    ScoreboardType.HOCKEY -> R.string.hockey_description
    ScoreboardType.SPIKEBALL -> R.string.spikeball_description
    ScoreboardType.TENNIS -> R.string.tennis_description
    ScoreboardType.BOXING -> R.string.boxing_description
}

fun ScoreboardType.intervalLabelRes() = when(this) {
    ScoreboardType.BASKETBALL -> R.string.quarter
    ScoreboardType.HOCKEY -> R.string.period
    ScoreboardType.SPIKEBALL -> R.string.game
    ScoreboardType.TENNIS -> R.string.game
    ScoreboardType.BOXING -> R.string.round
}

fun ScoreboardType.secondaryScoreLabelRes() = when(this) {
    ScoreboardType.BASKETBALL -> R.string.fouls
    ScoreboardType.HOCKEY -> R.string.penalties
    else -> R.string.blank
}