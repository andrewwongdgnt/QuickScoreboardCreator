package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardType


data class DefaultScoreboardConfig(

    val scoreboardType: ScoreboardType

) : ScoreboardConfig()
