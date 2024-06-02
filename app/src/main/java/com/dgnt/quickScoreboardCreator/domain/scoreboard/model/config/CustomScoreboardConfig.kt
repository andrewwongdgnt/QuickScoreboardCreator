package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

data class CustomScoreboardConfig(

    val title: String,
    val description: String,
    val intervalLabel: String,

) : ScoreboardConfig()