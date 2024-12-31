package com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping

import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon

fun SportIcon.iconRes() = when (this) {
    SportIcon.BASKETBALL -> R.drawable.basketball
    SportIcon.BOXING -> R.drawable.boxing
    SportIcon.HOCKEY -> R.drawable.hockey
    SportIcon.SOCCER -> R.drawable.soccer
    SportIcon.TENNIS -> R.drawable.tennis
    SportIcon.SPIKEBALL -> R.drawable.spikeball
    SportIcon.VOLLEYBALL -> R.drawable.volleyball
}