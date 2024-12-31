package com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping

import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule

fun WinRule.titleRes() = when (this) {
    WinRule.Count -> R.string.winRuleCount
    WinRule.Final -> R.string.winRuleFinal
    WinRule.Sum -> R.string.winRuleSum
}
