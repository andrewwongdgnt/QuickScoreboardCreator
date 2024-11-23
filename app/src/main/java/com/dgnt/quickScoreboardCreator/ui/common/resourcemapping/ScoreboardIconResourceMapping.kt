package com.dgnt.quickScoreboardCreator.ui.common.resourcemapping

import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon

fun ScoreboardIcon.iconRes() = when (this) {
    ScoreboardIcon.BASKETBALL -> R.drawable.basketball
    ScoreboardIcon.BOXING -> R.drawable.boxing
    ScoreboardIcon.HOCKEY -> R.drawable.hockey
    ScoreboardIcon.SOCCER -> R.drawable.soccer
    ScoreboardIcon.TENNIS -> R.drawable.tennis
    ScoreboardIcon.SPIKEBALL -> R.drawable.spikeball
    ScoreboardIcon.VOLLEYBALL -> R.drawable.volleyball
}