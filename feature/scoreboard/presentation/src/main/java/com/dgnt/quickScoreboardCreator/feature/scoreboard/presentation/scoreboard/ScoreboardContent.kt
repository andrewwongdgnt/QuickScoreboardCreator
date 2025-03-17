package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.util.PreviewLandscape
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector.rememberTimeline
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.PlaySound
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.mode.ModeControlContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.score.ScoreControl
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.score.TwoScoreDisplay
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.teamdisplay.TeamDisplayContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.time.IntervalDisplayContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.time.TimerControlContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.time.TimerDisplayContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ScoreboardContent(
    updatedTeamData: UpdatedTeamData?,
    updatedIntervalData: UpdatedIntervalData?,
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardViewModel = hiltViewModel()
) {

    updatedTeamData?.let { viewModel.onAction(ScoreboardAction.TeamPick(it)) }
    updatedIntervalData?.let { viewModel.onAction(ScoreboardAction.IntervalEdit(it)) }

    ScoreboardVMDataContent(
        onUiEvent = onUiEvent,
        viewModel = viewModel
    )
}

@Composable
private fun ScoreboardVMDataContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScoreboardInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ScoreboardInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: ScoreboardState,
    onAction: (ScoreboardAction) -> Unit,
) = state.run {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is PlaySound -> {
                    val mMediaPlayer = MediaPlayer.create(context, event.soundRes)
                    mMediaPlayer.start()
                }

                else -> onUiEvent(event)
            }
        }
    }

    val currentTeamSize = teamList.size
    val layoutSpacing = 10.dp
    if (currentTeamSize == 2) {
        Row(
            modifier = Modifier.padding(layoutSpacing)
        ) {
            val spacerWidth = 10.dp
            ScoreControl(
                modifier = Modifier.fillMaxHeight(),
                simpleMode = simpleMode,
                incrementList = primaryIncrementList[0],
                onIncrement = { index, main ->
                    onAction(ScoreboardAction.ScoreChange(true, 0, index, main))
                }
            )
            Spacer(modifier = Modifier.width(spacerWidth))
            TwoScoreDisplay(
                modifier = Modifier.weight(1f),
                simpleMode = simpleMode,
                primaryDisplayedScoreInfo = primaryDisplayedScoreInfo,
                secondaryDisplayedScoreInfo = secondaryDisplayedScoreInfo,
                secondaryIncrementList = secondaryIncrementList,
                secondaryScoreLabel = secondaryScoreLabel,
                onSecondaryScoreChange = { scoreIndex, incrementIndex, main ->
                    onAction(ScoreboardAction.ScoreChange(false, scoreIndex, incrementIndex, main))
                }
            )
            Spacer(modifier = Modifier.width(spacerWidth))
            ScoreControl(
                modifier = Modifier.fillMaxHeight(),
                simpleMode = simpleMode,
                incrementList = primaryIncrementList[1],
                onIncrement = { index, main ->
                    onAction(ScoreboardAction.ScoreChange(true, 1, index, main))
                }
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(layoutSpacing)
    ) {
        TimerControlContent(
            modifier = Modifier
                .align(Alignment.TopStart),
            timerInProgress = timerInProgress,
            onTimerPause = { onAction(ScoreboardAction.TimerPause(it)) },
            onTimerStart = { onAction(ScoreboardAction.TimerStart) },
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerDisplayContent(
                simpleMode = simpleMode,
                timeData = timeData,
                toIntervalEditor = { onAction(ScoreboardAction.ToIntervalEditor) },
            )
            IntervalDisplayContent(
                modifier = Modifier,
                intervalLabel = intervalLabel,
                currentInterval = currentInterval
            )

        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = rememberTimeline(),
                contentDescription = stringResource(id = R.string.timeline),
                modifier = Modifier
                    .clickable(onClick = { onAction(ScoreboardAction.ToTimelineViewer) })
            )
            Spacer(modifier = Modifier.width(5.dp))
            ModeControlContent(
                simpleMode = simpleMode,
                onToggleModeChange = { onAction(ScoreboardAction.ToggleModeChange(it)) },
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            TeamDisplayContent(
                simpleMode = simpleMode,
                teamDisplay = teamList[0],
                teamNumber = 1,
                onEditClick = { onAction(ScoreboardAction.ToTeamPicker(0)) },
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            TeamDisplayContent(
                simpleMode = simpleMode,
                teamDisplay = teamList[1],
                teamNumber = 2,
                onEditClick = { onAction(ScoreboardAction.ToTeamPicker(1)) },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@PreviewLandscape
@Composable
private fun ScoreboardContentPreview(
    @PreviewParameter(ScoreboardPreviewStateProvider::class) state: ScoreboardState
) = QuickScoreboardCreatorTheme {
    Surface {
        ScoreboardInnerContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}