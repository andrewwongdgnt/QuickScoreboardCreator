package com.dgnt.quickScoreboardCreator.feature.history.domain.model


data class HistoricalScore(
    val score: Int,
    val displayedScore: String,
    val time: Long
)
