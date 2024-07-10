package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.common.Label
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.rememberTimeline
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedIntervalData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedTeamData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.mode.ModeControlContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.score.ScoreControl
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.score.TwoScoreDisplay
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay.TeamDisplay
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay.TeamDisplayContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time.IntervalDisplayContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time.TimerControlContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time.TimerDisplayContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ScoreboardInteractionContent(
    updatedTeamData: UpdatedTeamData?,
    updatedIntervalData: UpdatedIntervalData?,
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardInteractionViewModel = hiltViewModel()
) {

    updatedTeamData?.let { viewModel.onTeamPick(it) }
    updatedIntervalData?.let { viewModel.onIntervalEdit(it) }

    ScoreboardInteractionVMDataContent(
        onUiEvent = onUiEvent,
        viewModel = viewModel
    )
}

@Composable
private fun ScoreboardInteractionVMDataContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardInteractionViewModel
) {
    val primaryDisplayedScoreInfo by viewModel.primaryDisplayedScoreInfo.collectAsStateWithLifecycle()
    val primaryIncrementList by viewModel.primaryIncrementList.collectAsStateWithLifecycle()
    val secondaryDisplayedScoreInfo by viewModel.secondaryDisplayedScoreInfo.collectAsStateWithLifecycle()
    val secondaryIncrementList by viewModel.secondaryIncrementList.collectAsStateWithLifecycle()
    val secondaryScoreLabelInfo by viewModel.secondaryScoreLabel.collectAsStateWithLifecycle()
    val teamList by viewModel.teamList.collectAsStateWithLifecycle()
    val timeData by viewModel.timeData.collectAsStateWithLifecycle()
    val timerInProgress by viewModel.timerInProgress.collectAsStateWithLifecycle()
    val simpleMode by viewModel.simpleMode.collectAsStateWithLifecycle()
    val intervalLabelInfo by viewModel.intervalLabel.collectAsStateWithLifecycle()
    val currentInterval by viewModel.currentInterval.collectAsStateWithLifecycle()

    ScoreboardInteractionInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        primaryDisplayedScoreInfo = primaryDisplayedScoreInfo,
        primaryIncrementList = primaryIncrementList,
        secondaryDisplayedScoreInfo = secondaryDisplayedScoreInfo,
        secondaryIncrementList = secondaryIncrementList,
        secondaryScoreLabel = secondaryScoreLabelInfo,
        onScoreChange = viewModel::onScoreChange,
        teamList = teamList,
        toTeamPicker = viewModel::toTeamPicker,
        timeData = timeData,
        timerInProgress = timerInProgress,
        onTimerPause = viewModel::onTimerPause,
        onTimerStart = viewModel::onTimerStart,
        simpleMode = simpleMode,
        onToggleModeChange = viewModel::onToggleModeChange,
        intervalLabel = intervalLabelInfo,
        currentInterval = currentInterval,
        toIntervalEditor = viewModel::toIntervalEditor,
        toTimelineViewer = viewModel::toTimelineViewer
    )
}

@Composable
private fun ScoreboardInteractionInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    primaryDisplayedScoreInfo: DisplayedScoreInfo,
    primaryIncrementList: List<List<Int>>,
    secondaryDisplayedScoreInfo: DisplayedScoreInfo,
    secondaryIncrementList: List<List<Int>>,
    secondaryScoreLabel: Label,
    onScoreChange: (Boolean, Int, Int, Boolean) -> Unit,
    teamList: List<TeamDisplay>,
    toTeamPicker: (Int) -> Unit,
    timeData: TimeData,
    timerInProgress: Boolean,
    onTimerPause: (Boolean) -> Unit,
    onTimerStart: () -> Unit,
    simpleMode: Boolean,
    onToggleModeChange: (Boolean) -> Unit,
    intervalLabel: Label,
    currentInterval: Int,
    toIntervalEditor: () -> Unit,
    toTimelineViewer: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.PlaySound -> {
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
                onIncrement = { index, positive ->
                    onScoreChange(true, 0, index, positive)
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
                onScoreChange = onScoreChange
            )
            Spacer(modifier = Modifier.width(spacerWidth))
            ScoreControl(
                modifier = Modifier.fillMaxHeight(),
                simpleMode = simpleMode,
                incrementList = primaryIncrementList[1],
                onIncrement = { index, positive ->
                    onScoreChange(true, 1, index, positive)
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
            onTimerPause = onTimerPause,
            onTimerStart = onTimerStart,
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerDisplayContent(
                simpleMode = simpleMode,
                timeData = timeData,
                toIntervalEditor = toIntervalEditor,
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
                    .clickable(onClick = toTimelineViewer)
            )
            Spacer(modifier = Modifier.width(5.dp))
            ModeControlContent(
                simpleMode = simpleMode,
                onToggleModeChange = onToggleModeChange,
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
                onEditClick = { toTeamPicker(0) },
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            TeamDisplayContent(
                simpleMode = simpleMode,
                teamDisplay = teamList[1],
                teamNumber = 2,
                onEditClick = { toTeamPicker(1) },
                modifier = Modifier
                    .weight(1f)
            )
        }

    }


}

@PreviewScreenSizes
@Composable
private fun `2 Teams with long names`() =
    ScoreboardInteractionInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        primaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("10"),
                DisplayedScore.CustomDisplayedScore("21"),
            ),
            DisplayedScore.Blank
        ),
        primaryIncrementList = listOf(
            listOf(1, 2, 2),
            listOf(1, 2, 3),
        ),
        secondaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("1"),
                DisplayedScore.CustomDisplayedScore("0"),
            ),
            DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(
            listOf(1),
            listOf(1),
        ),
        secondaryScoreLabel = Label.CustomLabel("S"),
        onScoreChange = { _, _, _, _ -> },
        teamList = listOf(
            TeamDisplay.SelectedTeamDisplay("Gorillas Gorillas Gorillas Gorilla Gorillas Gorill", TeamIcon.GORILLA),
            TeamDisplay.SelectedTeamDisplay("Tigers Tigers Tigers Tigers Tigers", TeamIcon.TIGER)
        ),
        toTeamPicker = { _ -> },
        timeData = TimeData(12, 2, 4),
        timerInProgress = false,
        onTimerPause = { _ -> },
        onTimerStart = {},
        simpleMode = false,
        onToggleModeChange = { _ -> },
        intervalLabel = Label.CustomLabel("P"),
        currentInterval = 1,
        toIntervalEditor = {},
        toTimelineViewer = {},
    )

@PreviewScreenSizes
@Composable
private fun `2 Teams with short names`() =
    ScoreboardInteractionInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        primaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("10"),
                DisplayedScore.CustomDisplayedScore("261"),
            ),
            DisplayedScore.Blank
        ),
        primaryIncrementList = listOf(
            listOf(1, 2, 23),
            listOf(1, 2, 3),
        ),
        secondaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("1"),
                DisplayedScore.CustomDisplayedScore("0"),
            ),
            DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(
            listOf(1),
            listOf(1),
        ),
        secondaryScoreLabel = Label.ResourceLabel(R.string.fouls),
        onScoreChange = { _, _, _, _ -> },
        teamList = listOf(
            TeamDisplay.SelectedTeamDisplay("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.SelectedTeamDisplay("Tigers", TeamIcon.TIGER)
        ),
        toTeamPicker = { _ -> },
        timeData = TimeData(12, 2, 4),
        timerInProgress = false,
        onTimerPause = { _ -> },
        onTimerStart = {},
        simpleMode = true,
        onToggleModeChange = { _ -> },
        intervalLabel = Label.ResourceLabel(R.string.quarter),
        currentInterval = 1,
        toIntervalEditor = {},
        toTimelineViewer = {},
    )

@PreviewScreenSizes
@Composable
private fun `Adv`() =
    ScoreboardInteractionInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        primaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Advantage,
                DisplayedScore.Blank,
            ),
            DisplayedScore.Blank
        ),
        primaryIncrementList = listOf(
            listOf(1, 2, 23),
            listOf(1, 2, 3),
        ),
        secondaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(),
            DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(),
        secondaryScoreLabel = Label.ResourceLabel(R.string.blank),
        onScoreChange = { _, _, _, _ -> },
        teamList = listOf(
            TeamDisplay.SelectedTeamDisplay("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.SelectedTeamDisplay("Tigers", TeamIcon.TIGER)
        ),
        toTeamPicker = { _ -> },
        timeData = TimeData(12, 2, 4),
        timerInProgress = false,
        onTimerPause = { _ -> },
        onTimerStart = {},
        simpleMode = true,
        onToggleModeChange = { _ -> },
        intervalLabel = Label.ResourceLabel(R.string.set),
        currentInterval = 2,
        toIntervalEditor = {},
        toTimelineViewer = {},
    )

@PreviewScreenSizes
@Composable
private fun `Deuce`() =
    ScoreboardInteractionInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        primaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Blank,
                DisplayedScore.Blank,
            ),
            DisplayedScore.Deuce
        ),
        primaryIncrementList = listOf(
            listOf(1, 2, 23),
            listOf(1, 2, 3),
        ),
        secondaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(),
            DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(),
        secondaryScoreLabel = Label.ResourceLabel(R.string.blank),
        onScoreChange = { _, _, _, _ -> },
        teamList = listOf(
            TeamDisplay.SelectedTeamDisplay("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.SelectedTeamDisplay("Tigers", TeamIcon.TIGER)
        ),
        toTeamPicker = { _ -> },
        timeData = TimeData(12, 2, 4),
        timerInProgress = false,
        onTimerPause = { _ -> },
        onTimerStart = {},
        simpleMode = false,
        onToggleModeChange = { _ -> },
        intervalLabel = Label.ResourceLabel(R.string.game),
        currentInterval = 3,
        toIntervalEditor = {},
        toTimelineViewer = {},
    )