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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.util.PreviewLandscape
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector.rememberTimeline
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.PlaySound
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.mode.ModeControlContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.score.ScoreControl
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.score.TwoScoreDisplay
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.teamdisplay.TeamDisplay
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.teamdisplay.TeamDisplayContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.time.IntervalDisplayContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.time.TimerControlContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.time.TimerDisplayContent
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ScoreboardContent(
    updatedTeamData: UpdatedTeamData?,
    updatedIntervalData: UpdatedIntervalData?,
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardViewModel = hiltViewModel()
) {

    updatedTeamData?.let { viewModel.onTeamPick(it) }
    updatedIntervalData?.let { viewModel.onIntervalEdit(it) }

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

    ScoreboardInnerContent(
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
private fun ScoreboardInnerContent(
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
                    onScoreChange(true, 0, index, main)
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
                onIncrement = { index, main ->
                    onScoreChange(true, 1, index, main)
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

@PreviewLandscape
@Composable
private fun `2 Teams with long names`() =
    ScoreboardInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        primaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Custom("10"),
                DisplayedScore.Custom("21"),
            ),
            DisplayedScore.Blank
        ),
        primaryIncrementList = listOf(
            listOf(1, 2, 2),
            listOf(1, 2, 3),
        ),
        secondaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Custom("1"),
                DisplayedScore.Custom("0"),
            ),
            DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(
            listOf(1),
            listOf(1),
        ),
        secondaryScoreLabel = Label.Custom("S"),
        onScoreChange = { _, _, _, _ -> },
        teamList = listOf(
            TeamDisplay.Selected("Gorillas Gorillas Gorillas Gorilla Gorillas Gorill", TeamIcon.GORILLA),
            TeamDisplay.Selected("Tigers Tigers Tigers Tigers Tigers", TeamIcon.TIGER)
        ),
        toTeamPicker = { _ -> },
        timeData = TimeData(12, 2, 4),
        timerInProgress = false,
        onTimerPause = { _ -> },
        onTimerStart = {},
        simpleMode = false,
        onToggleModeChange = { _ -> },
        intervalLabel = Label.Custom("P"),
        currentInterval = 1,
        toIntervalEditor = {},
        toTimelineViewer = {},
    )

@PreviewLandscape
@Composable
private fun `2 Teams with short names`() =
    ScoreboardInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        primaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Custom("10"),
                DisplayedScore.Custom("261"),
            ),
            DisplayedScore.Blank
        ),
        primaryIncrementList = listOf(
            listOf(1, 2, 23),
            listOf(1, 2, 3),
        ),
        secondaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Custom("1"),
                DisplayedScore.Custom("0"),
            ),
            DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(
            listOf(1),
            listOf(1),
        ),
        secondaryScoreLabel = Label.Resource(R.string.fouls),
        onScoreChange = { _, _, _, _ -> },
        teamList = listOf(
            TeamDisplay.Selected("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.Selected("Tigers", TeamIcon.TIGER)
        ),
        toTeamPicker = { _ -> },
        timeData = TimeData(12, 2, 4),
        timerInProgress = false,
        onTimerPause = { _ -> },
        onTimerStart = {},
        simpleMode = true,
        onToggleModeChange = { _ -> },
        intervalLabel = Label.Resource(R.string.quarter),
        currentInterval = 1,
        toIntervalEditor = {},
        toTimelineViewer = {},
    )

@PreviewLandscape
@Composable
private fun `Adv`() =
    ScoreboardInnerContent(
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
        secondaryScoreLabel = Label.Resource(R.string.blank),
        onScoreChange = { _, _, _, _ -> },
        teamList = listOf(
            TeamDisplay.Selected("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.Selected("Tigers", TeamIcon.TIGER)
        ),
        toTeamPicker = { _ -> },
        timeData = TimeData(12, 2, 4),
        timerInProgress = false,
        onTimerPause = { _ -> },
        onTimerStart = {},
        simpleMode = true,
        onToggleModeChange = { _ -> },
        intervalLabel = Label.Resource(R.string.set),
        currentInterval = 2,
        toIntervalEditor = {},
        toTimelineViewer = {},
    )

@PreviewLandscape
@Composable
private fun `Deuce`() =
    ScoreboardInnerContent(
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
        secondaryScoreLabel = Label.Resource(R.string.blank),
        onScoreChange = { _, _, _, _ -> },
        teamList = listOf(
            TeamDisplay.Selected("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.Selected("Tigers", TeamIcon.TIGER)
        ),
        toTeamPicker = { _ -> },
        timeData = TimeData(12, 2, 4),
        timerInProgress = false,
        onTimerPause = { _ -> },
        onTimerStart = {},
        simpleMode = false,
        onToggleModeChange = { _ -> },
        intervalLabel = Label.Resource(R.string.game),
        currentInterval = 3,
        toIntervalEditor = {},
        toTimelineViewer = {},
    )