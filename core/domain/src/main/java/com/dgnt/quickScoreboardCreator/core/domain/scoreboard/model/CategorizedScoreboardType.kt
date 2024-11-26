package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.ScoreboardType

data class CategorizedScoreboardType(
    val scoreboardTypeList: List<ScoreboardType>
)