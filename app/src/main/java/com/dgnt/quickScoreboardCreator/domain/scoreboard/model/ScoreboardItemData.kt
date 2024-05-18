package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType

data class ScoreboardItemData(
    val id: Int,
    val type: ScoreboardType,
    val title: String,
    val description: String
)