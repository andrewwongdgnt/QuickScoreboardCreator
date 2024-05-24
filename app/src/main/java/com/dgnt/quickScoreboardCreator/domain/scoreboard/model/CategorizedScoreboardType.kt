package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType

data class CategorizedScoreboardType(
    val scoreboardTypeList: List<ScoreboardType>
)