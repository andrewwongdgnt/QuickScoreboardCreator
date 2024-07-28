package com.dgnt.quickScoreboardCreator.domain.history.model


data class HistoricalInterval(
    val range: HistoricalIntervalRange,
    val intervalLabel: IntervalLabel,
    val historicalScoreGroupList: Map<Int, HistoricalScoreGroup>
)

sealed interface HistoricalIntervalRange {

    data class CountDown(val start: Long) : HistoricalIntervalRange

    data object Infinite : HistoricalIntervalRange
}