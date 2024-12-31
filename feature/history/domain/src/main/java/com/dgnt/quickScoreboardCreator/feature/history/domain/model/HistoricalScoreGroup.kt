package com.dgnt.quickScoreboardCreator.feature.history.domain.model


data class HistoricalScoreGroup(
    val teamLabel: TeamLabel,
    val primaryScoreList: List<HistoricalScore>,
    val secondaryScoreList: List<HistoricalScore>,
)

