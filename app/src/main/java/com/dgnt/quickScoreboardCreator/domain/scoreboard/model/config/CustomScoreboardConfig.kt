package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon

data class CustomScoreboardConfig(

    val title: String,
    val description: String,
    val icon: ScoreboardIcon,
    val intervalLabel: String,

) : ScoreboardConfig()