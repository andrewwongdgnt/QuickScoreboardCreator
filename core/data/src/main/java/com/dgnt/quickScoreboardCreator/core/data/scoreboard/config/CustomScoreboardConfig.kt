package com.dgnt.quickScoreboardCreator.core.data.scoreboard.config

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon

data class CustomScoreboardConfig(

    val title: String,
    val description: String,
    val icon: ScoreboardIcon,
    val intervalLabel: String,

) : ScoreboardConfig()