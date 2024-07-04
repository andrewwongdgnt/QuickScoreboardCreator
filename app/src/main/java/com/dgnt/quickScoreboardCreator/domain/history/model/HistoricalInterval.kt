package com.dgnt.quickScoreboardCreator.domain.history.model

data class HistoricalInterval(
    val historicalScoreGroupList: Map<Int, HistoricalScoreGroup>
)
