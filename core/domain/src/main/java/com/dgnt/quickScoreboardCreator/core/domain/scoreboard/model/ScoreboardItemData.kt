package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model

data class ScoreboardItemData(
    val scoreboardIdentifier: ScoreboardIdentifier,
    val title: String,
    val description: String,
    val icon: ScoreboardIcon
)