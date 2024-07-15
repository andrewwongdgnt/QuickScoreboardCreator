@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun TimelineViewerContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TimelineViewerViewModel = hiltViewModel()
) {

    val historicalInterval by viewModel.historicalInterval.collectAsStateWithLifecycle()

    TimelineViewerInnerContent(
        viewModel.uiEvent,
        onUiEvent,
        historicalInterval,
        viewModel::onDismiss,
        viewModel::onSave
    )

}

@Composable
private fun TimelineViewerInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    historicalInterval: HistoricalInterval?,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    if (historicalInterval == null)
        CircularProgressIndicator(
            modifier = Modifier
                .size(72.dp)
                .padding(5.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    else
        TimelineViewerChart(historicalInterval = historicalInterval)

}

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun `Infinite Timeline`() =
    TimelineViewerInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        historicalInterval = HistoricalInterval(
            range = HistoricalIntervalRange.Infinite,
            IntervalLabel.CustomIntervalLabel("Game", 0),
            mapOf(
                0 to HistoricalScoreGroup(
                    teamLabel = TeamLabel.CustomTeamLabel("DGNT", TeamIcon.AXE),
                    primaryScoreList = listOf(
                        HistoricalScore(0, "0", 0),
                        HistoricalScore(1, "1", 1000),
                        HistoricalScore(2, "2", 1400),
                        HistoricalScore(3, "3", 6003),
                    ),
                    secondaryScoreList = listOf()
                ),
                1 to HistoricalScoreGroup(
                    teamLabel = TeamLabel.NoTeamLabel,
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
        ),
        onDismiss = {},
        onSave = {},
    )

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun `Countdown Timeline`() =
    TimelineViewerInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        historicalInterval = HistoricalInterval(
            range = HistoricalIntervalRange.CountDown(72000),
            IntervalLabel.CustomIntervalLabel("Quarter", 0),
            mapOf(
                0 to HistoricalScoreGroup(
                    teamLabel = TeamLabel.CustomTeamLabel("DGNT", TeamIcon.AXE),
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
        ),
        onDismiss = {},
        onSave = {},
    )



