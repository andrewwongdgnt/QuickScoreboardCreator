package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config


data class DefaultScoreboardConfig(

    override val type: String,
    override val scoreCarriesOver: Boolean,
    override val intervalList: List<IntervalListConfig>,
    val scoreboardType: ScoreboardType

) : ScoreboardConfig()

