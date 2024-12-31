package com.dgnt.quickScoreboardCreator.feature.history.domain.model


data class HistoricalScoreboard(
    val historicalIntervalMap: Map<Int, HistoricalInterval>
)
