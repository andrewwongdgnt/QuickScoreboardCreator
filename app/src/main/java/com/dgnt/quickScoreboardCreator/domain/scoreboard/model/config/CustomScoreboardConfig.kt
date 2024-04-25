package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

data class CustomScoreboardConfig(
    override val type: String,
    override val scoreCarriesOver: Boolean,
    override val intervalList: List<IntervalListConfig>,
    val title: String,
    val description: String

) : ScoreboardConfig()