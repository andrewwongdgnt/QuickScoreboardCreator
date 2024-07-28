package com.dgnt.quickScoreboardCreator.domain.history.model


data class HistoricalScoreGroup(
    val teamLabel: TeamLabel,
    val primaryScoreList: List<HistoricalScore>,
    val secondaryScoreList: List<HistoricalScore>,
)

