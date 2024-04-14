package com.dgnt.quickScoreboardCreator.domain.model.config


data class ScoreboardTemplate(

    override val type: String,
    override val scoreCarriesOver: Boolean,
    override val intervalList: List<IntervalListItem>,
    val scoreboardType: ScoreboardType

) : ScoreboardConfig()

