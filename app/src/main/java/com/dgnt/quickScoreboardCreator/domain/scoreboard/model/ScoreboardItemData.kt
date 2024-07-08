package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier

data class ScoreboardItemData(
    val scoreboardIdentifier: ScoreboardIdentifier,
    val title: String,
    val description: String,
    val icon: ScoreboardIcon
)