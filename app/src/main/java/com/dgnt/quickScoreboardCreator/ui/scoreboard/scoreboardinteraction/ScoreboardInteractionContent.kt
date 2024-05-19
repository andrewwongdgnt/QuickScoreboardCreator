package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedIntervalData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedTeamData
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

    updatedTeamData?.let { viewModel.onEvent(ScoreboardInteractionEvent.UpdatedTeam(it)) }
    updatedIntervalData?.let { viewModel.onEvent(ScoreboardInteractionEvent.UpdatedInterval(it)) }

    ScoreboardInteractionVMDataContent(
        onUiEvent,
        viewModel
    )
}

@Composable
private fun ScoreboardInteractionVMDataContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardInteractionViewModel
) {
    val timeData = viewModel.timeData
    val timerInProgress = viewModel.timerInProgress
    val incrementList = viewModel.incrementList
    val displayedScoreInfo = viewModel.displayedScoreInfo
    val teamList = viewModel.teamList
    val labelInfo = viewModel.labelInfo
    val currentInterval = viewModel.currentInterval

    ScoreboardInteractionInnerContent(
        viewModel.uiEvent,
        onUiEvent,
        incrementList,
        teamList,
        displayedScoreInfo,
        timeData,
        timerInProgress,
        labelInfo,
        currentInterval,
        viewModel::onEvent
    )
}

@Composable
private fun ScoreboardInteractionInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    incrementList: List<List<Int>>,
    teamList: List<TeamDisplay>,
    displayedScoreInfo: DisplayedScoreInfo,
    timeData: TimeData,
    timerInProgress: Boolean,
    labelInfo: Pair<String?, Int?>,
    currentInterval: Int,
    onEvent: (ScoreboardInteractionEvent) -> Unit
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    val currentTeamSize = incrementList.size
    val layoutSpacing = 10.dp
    if (currentTeamSize == 2) {
        Row(
            modifier = Modifier.padding(layoutSpacing)
        ) {
            val spacerWidth = 10.dp
            ScoreControl(
                incrementList = incrementList[0],
                onIncrement = { index, positive ->
                    onEvent(ScoreboardInteractionEvent.UpdateScore(0, index, positive))
                }
            )
            Spacer(modifier = Modifier.width(spacerWidth))
            TwoScoreDisplay(
                modifier = Modifier.weight(1f),
                displayedScoreInfo
            )
            Spacer(modifier = Modifier.width(spacerWidth))
            ScoreControl(
                incrementList = incrementList[1],
                onIncrement = { index, positive ->
                    onEvent(ScoreboardInteractionEvent.UpdateScore(1, index, positive))
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
            timerInProgress = timerInProgress,
            onEvent = onEvent,
            modifier = Modifier
                .align(Alignment.TopStart)
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerDisplayContent(
                timeData = timeData,
                onEvent = onEvent,
            )
            IntervalDisplayContent(
                modifier = Modifier,
                labelInfo = labelInfo,
                currentInterval = currentInterval
            )

        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            TeamDisplayContent(
                teamDisplay = teamList[0],
                onEditClick = { onEvent(ScoreboardInteractionEvent.UpdateTeam(0)) },
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            TeamDisplayContent(
                teamDisplay = teamList[1],
                onEditClick = { onEvent(ScoreboardInteractionEvent.UpdateTeam(1)) },
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
        emptyFlow(),
        {},
        listOf(
            listOf(1, 2, 2),
            listOf(1, 2, 3),
        ),
        listOf(
            TeamDisplay.SelectedTeamDisplay("Gorillas Gorillas Gorillas Gorilla Gorillas Gorill", TeamIcon.GORILLA),
            TeamDisplay.SelectedTeamDisplay("Tigers Tigers Tigers Tigers Tigers", TeamIcon.TIGER)
        ),
        DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("10"),
                DisplayedScore.CustomDisplayedScore("21"),
            ),
            DisplayedScore.Blank
        ),
        TimeData(12, 2, 4),
        false,
        Pair("P", null),
        1
    ) {}

@PreviewScreenSizes
@Composable
private fun `2 Teams with short names`() =
    ScoreboardInteractionInnerContent(
        emptyFlow(),
        {},
        listOf(
            listOf(1, 2, 23),
            listOf(1, 2, 3),
        ),
        listOf(
            TeamDisplay.SelectedTeamDisplay("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.SelectedTeamDisplay("Tigers", TeamIcon.TIGER)
        ),
        DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("10"),
                DisplayedScore.CustomDisplayedScore("261"),
            ),
            DisplayedScore.Blank
        ),
        TimeData(12, 2, 4),
        false,
        Pair(null, R.string.quarter),
        1
    ) {}

@PreviewScreenSizes
@Composable
private fun `Adv`() =
    ScoreboardInteractionInnerContent(
        emptyFlow(),
        {},
        listOf(
            listOf(1, 2, 23),
            listOf(1, 2, 3),
        ),
        listOf(
            TeamDisplay.SelectedTeamDisplay("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.SelectedTeamDisplay("Tigers", TeamIcon.TIGER)
        ),
        DisplayedScoreInfo(
            listOf(
                DisplayedScore.Advantage,
                DisplayedScore.Blank,
            ),
            DisplayedScore.Blank
        ),
        TimeData(12, 2, 4),
        false,
        Pair(null, R.string.set),
        2
    ) {}

@PreviewScreenSizes
@Composable
private fun `Deuce`() =
    ScoreboardInteractionInnerContent(
        emptyFlow(),
        {},
        listOf(
            listOf(1, 2, 23),
            listOf(1, 2, 3),
        ),
        listOf(
            TeamDisplay.SelectedTeamDisplay("Gorillas", TeamIcon.GORILLA),
            TeamDisplay.SelectedTeamDisplay("Tigers", TeamIcon.TIGER)
        ),
        DisplayedScoreInfo(
            listOf(
                DisplayedScore.Blank,
                DisplayedScore.Blank,
            ),
            DisplayedScore.Deuce
        ),
        TimeData(12, 2, 4),
        false,
        Pair(null, R.string.game),
        3
    ) {}