package com.dgnt.quickScoreboardCreator.domain.history.model


data class HistoricalScoreboard(
    val historicalIntervalMap: Map<Int, HistoricalInterval>
)
