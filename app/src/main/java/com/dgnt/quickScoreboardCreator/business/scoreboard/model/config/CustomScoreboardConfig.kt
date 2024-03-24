package com.dgnt.quickScoreboardCreator.business.scoreboard.model.config

data class CustomScoreboardConfig(
    override val type: String,
    override val scoreCarriesOver: Boolean,
    override val intervalList: List<IntervalListItem>,
    val name: String,
    val description: String

) : ScoreboardConfig()