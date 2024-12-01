@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.core.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.core.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.util.PreviewLandscape
import com.dgnt.quickScoreboardCreator.ui.common.resourcemapping.intervalLabelRes
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
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
        viewModel::onNewInterval
    )

}

@Composable
private fun TimelineViewerInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    historicalInterval: HistoricalInterval?,
    onDismiss: () -> Unit,
    onNewInterval: (Boolean) -> Unit
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (historicalInterval == null)
            CircularProgressIndicator(
                modifier = Modifier
                    .size(72.dp)
                    .padding(5.dp)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        else {
            TimelineViewerChart(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 30.dp)
                    .fillMaxSize()
                    .align(Alignment.Center),
                historicalInterval = historicalInterval
            )
            val intervalLabel = historicalInterval.let {
                when (val label = historicalInterval.intervalLabel) {
                    is IntervalLabel.Custom -> label.value
                    is IntervalLabel.ScoreboardType -> stringResource(id = label.scoreboardType.intervalLabelRes())
                }
            }
            val intervalLabelString = stringResource(
                id = R.string.intervalLabel, intervalLabel, historicalInterval.intervalLabel.index + 1

            )
            Image(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clickable(onClick = onDismiss)
                    .padding(10.dp)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.prevInterval),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .clickable {
                            onNewInterval(false)
                        }
                )
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = intervalLabelString,
                    style = MaterialTheme.typography.titleLarge,
                )
                Image(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.nextInterval),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .clickable {
                            onNewInterval(true)
                        }
                )
            }
        }
    }

}


@PreviewLandscape
@Composable
private fun `Loading Timeline`() =
    TimelineViewerInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        historicalInterval = null,
        onDismiss = {},
        onNewInterval = {},
    )

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
        ),
        onDismiss = {},
        onNewInterval = {},
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
        ),
        onDismiss = {},
        onNewInterval = {},
    )



