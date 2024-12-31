package com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping

import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType

fun SportType.rawRes() = when (this) {
    SportType.BASKETBALL -> R.raw.basketball
    SportType.BOXING -> R.raw.boxing
    SportType.HOCKEY -> R.raw.hockey
    SportType.TENNIS -> R.raw.tennis
    SportType.SPIKEBALL -> R.raw.spikeball
}

fun SportType.titleRes() = when(this) {
    SportType.BASKETBALL -> R.string.basketball
    SportType.HOCKEY -> R.string.hockey
    SportType.SPIKEBALL -> R.string.spikeball
    SportType.TENNIS -> R.string.tennis
    SportType.BOXING -> R.string.boxing
}

fun SportType.descriptionRes() = when(this) {
    SportType.BASKETBALL -> R.string.basketball_description
    SportType.HOCKEY -> R.string.hockey_description
    SportType.SPIKEBALL -> R.string.spikeball_description
    SportType.TENNIS -> R.string.tennis_description
    SportType.BOXING -> R.string.boxing_description
}

fun SportType.intervalLabelRes() = when(this) {
    SportType.BASKETBALL -> R.string.quarter
    SportType.HOCKEY -> R.string.period
    SportType.SPIKEBALL -> R.string.game
    SportType.TENNIS -> R.string.game
    SportType.BOXING -> R.string.round
}

fun SportType.secondaryScoreLabelRes() = when(this) {
    SportType.BASKETBALL -> R.string.fouls
    SportType.HOCKEY -> R.string.penalties
    else -> R.string.blank
}