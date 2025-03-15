package com.dgnt.quickScoreboardCreator.feature.history.presentation.timelineviewer

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon

class TimelineViewerPreviewStateProvider : CollectionPreviewParameterProvider<TimelineViewerState>(
    listOf(
        TimelineViewerState(
            historicalIntervalState = HistoricalIntervalState.Initial
        ),
        TimelineViewerState(
            historicalIntervalState = HistoricalIntervalState.None
        ),
        TimelineViewerState(
            historicalIntervalState = HistoricalIntervalState.Loaded(
                HistoricalInterval(
                    range = HistoricalIntervalRange.Infinite,
                    IntervalLabel.Custom("Game", 0),
                    mapOf(
                        0 to HistoricalScoreGroup(
                            teamLabel = TeamLabel.Custom("DGNT", TeamIcon.AXE),
                            primaryScoreList = listOf(
                                HistoricalScore(0, "0", 0),
                                HistoricalScore(1, "1", 1000),
                                HistoricalScore(2, "2", 1400),
                                HistoricalScore(3, "3", 6003),
                            ),
                            secondaryScoreList = listOf()
                        ),
                        1 to HistoricalScoreGroup(
                            teamLabel = TeamLabel.None,
                            primaryScoreList = listOf(
                                HistoricalScore(0, "0", 0),
                                HistoricalScore(1, "1", 2000),
                                HistoricalScore(2, "2", 4400),
                                HistoricalScore(3, "3", 5655),
                                HistoricalScore(4, "4", 9800),
                            ),
                            secondaryScoreList = listOf()
                        )
                    )
                )
            )
        ),
        TimelineViewerState(
            historicalIntervalState = HistoricalIntervalState.Loaded(
                HistoricalInterval(
                    range = HistoricalIntervalRange.CountDown(72000),
                    IntervalLabel.Custom("Quarter", 0),
                    mapOf(
                        0 to HistoricalScoreGroup(
                            teamLabel = TeamLabel.Custom("DGNT", TeamIcon.AXE),
                            primaryScoreList = listOf(
                                HistoricalScore(0, "0", 720000),
                                HistoricalScore(1, "1", 66000L),
                                HistoricalScore(2, "2", 63000L),
                                HistoricalScore(3, "3", 480000),
                                HistoricalScore(4, "4", 330000),
                                HistoricalScore(7, "7", 300000),
                            ),
                            secondaryScoreList = listOf()
                        )
                    )
                )
            )
        ),
    )
)