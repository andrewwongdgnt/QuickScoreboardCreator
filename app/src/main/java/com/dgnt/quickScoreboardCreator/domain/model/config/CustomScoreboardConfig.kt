package com.dgnt.quickScoreboardCreator.domain.model.config

data class CustomScoreboardConfig(
    override val type: String,
    override val scoreCarriesOver: Boolean,
    override val intervalList: List<IntervalListItem>,
    val name: String,
    val description: String

) : ScoreboardConfig()