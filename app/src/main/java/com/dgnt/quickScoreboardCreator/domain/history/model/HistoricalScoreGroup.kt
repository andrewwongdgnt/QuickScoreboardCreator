package com.dgnt.quickScoreboardCreator.domain.history.model

data class HistoricalScoreGroup(
    val primaryScoreList: List<HistoricalScore>,
    val secondaryScoreList: List<HistoricalScore>,
)

