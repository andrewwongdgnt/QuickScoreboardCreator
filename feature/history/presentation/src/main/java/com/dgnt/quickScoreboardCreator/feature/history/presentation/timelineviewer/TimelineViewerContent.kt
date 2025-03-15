package com.dgnt.quickScoreboardCreator.feature.history.presentation.timelineviewer


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.util.PreviewLandscape
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.intervalLabelRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun TimelineViewerContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TimelineViewerViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    TimelineViewerInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
private fun TimelineViewerInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: TimelineViewerState,
    onAction: (TimelineViewerAction) -> Unit
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (val historicalIntervalState = state.historicalIntervalState) {
            is HistoricalIntervalState.Loaded -> {
                TimelineViewerChart(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 30.dp)
                        .fillMaxSize()
                        .align(Alignment.Center),
                    historicalInterval = historicalIntervalState.historicalInterval
                )
                val intervalLabel = historicalIntervalState.historicalInterval.let {
                    when (val label = it.intervalLabel) {
                        is IntervalLabel.Custom -> label.value
                        is IntervalLabel.DefaultSport -> stringResource(id = label.sportType.intervalLabelRes())
                    }
                }
                val intervalLabelString = stringResource(
                    id = R.string.intervalLabel, intervalLabel, historicalIntervalState.historicalInterval.intervalLabel.index + 1

                )
                Image(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clickable(onClick = { onAction(TimelineViewerAction.Dismiss) })
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
                                onAction(TimelineViewerAction.NewInterval(false))
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
                                onAction(TimelineViewerAction.NewInterval(true))
                            }
                    )
                }
            }

            is HistoricalIntervalState.Initial ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(72.dp)
                        .padding(5.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )

            is HistoricalIntervalState.None ->
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    text = stringResource(R.string.noTimelineContentMsg),
                    style = MaterialTheme.typography.titleLarge,
                )


        }

    }

}

@PreviewLandscape
@Composable
private fun TimelineViewerContentPreview(
    @PreviewParameter(TimelineViewerPreviewStateProvider::class) state: TimelineViewerState
) = QuickScoreboardCreatorTheme {
    Surface {
        TimelineViewerInnerContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}
