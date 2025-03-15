package com.dgnt.quickScoreboardCreator.feature.history.presentation.timelineviewer

import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval

data class TimelineViewerState(
    val historicalIntervalState: HistoricalIntervalState = HistoricalIntervalState.Initial,
)

sealed interface HistoricalIntervalState {
    data object Initial : HistoricalIntervalState
    data object None : HistoricalIntervalState
    data class Loaded(val historicalInterval: HistoricalInterval) : HistoricalIntervalState
}