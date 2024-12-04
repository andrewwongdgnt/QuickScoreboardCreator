package com.dgnt.quickScoreboardCreator.core.domain.history.model


data class HistoricalScoreboard(
    val historicalIntervalMap: Map<Int, HistoricalInterval>
)
