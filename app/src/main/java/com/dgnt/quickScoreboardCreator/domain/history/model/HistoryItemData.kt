package com.dgnt.quickScoreboardCreator.domain.history.model

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon

data class HistoryItemData(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ScoreboardIcon
)