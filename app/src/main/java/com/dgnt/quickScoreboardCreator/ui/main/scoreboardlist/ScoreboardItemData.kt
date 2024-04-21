package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardType

data class ScoreboardItemData(
    val id: Int?,
    val type: ScoreboardType?,
    val title: String,
    val description: String
)