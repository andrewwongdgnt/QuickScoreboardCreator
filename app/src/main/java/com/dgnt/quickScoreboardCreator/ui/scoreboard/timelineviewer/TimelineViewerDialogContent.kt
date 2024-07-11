@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.DefaultAlertDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun TimelineViewerDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TimelineViewerViewModel = hiltViewModel()
) {

    val historicalScoreboard by viewModel.historicalScoreboard.collectAsStateWithLifecycle()

    TimelineViewerInnerDialogContent(
        viewModel.uiEvent,
        onUiEvent,
        historicalScoreboard,
        viewModel::onDismiss,
        viewModel::onSave
    )

}

@Composable
private fun TimelineViewerInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    historicalScoreboard: HistoricalScoreboard?,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    DefaultAlertDialog(
        title = stringResource(id = R.string.timelineViewerTitle),
        confirmText = stringResource(id = R.string.save),
        onConfirm = onSave,
        dismissText = stringResource(id = android.R.string.cancel),
        onDismiss = onDismiss
    ) {
        if (historicalScoreboard == null) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(72.dp)
                    .padding(5.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    items = historicalScoreboard.historicalIntervalMap.entries.toList(),
                    key = { it.hashCode() }
                ) {
                    TimelineViewerChart(historicalInterval = it.value)
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun `Regular Timeline`() =
    TimelineViewerInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        historicalScoreboard = HistoricalScoreboard(
            mapOf(
                0 to HistoricalInterval(
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
                )
            )
        ),
        onDismiss = {},
        onSave = {},
    )



