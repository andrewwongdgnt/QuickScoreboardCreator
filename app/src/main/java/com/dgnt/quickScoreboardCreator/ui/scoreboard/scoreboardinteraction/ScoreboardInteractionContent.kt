package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.TeamSelectedData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.score.DefaultScoreContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay.TeamDisplay
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay.TeamDisplayContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time.TimeControlContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time.TimeDisplayContent

@Composable
fun ScoreboardInteractionContent(
    teamSelectedData: TeamSelectedData,
    toTeamPicker: (UiEvent.TeamPicker) -> Unit,
    viewModel: ScoreboardInteractionViewModel = hiltViewModel()
) {

    val timeData = viewModel.timeData
    val timerInProgress = viewModel.timerInProgress
    val incrementList = viewModel.incrementList
    val displayedScoreInfo = viewModel.displayedScoreInfo
    val teamList = viewModel.teamList
    viewModel.onEvent(ScoreboardInteractionEvent.SetTeam(teamSelectedData))
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.TeamPicker -> toTeamPicker(it)
                else -> Unit
            }
        }
    }
    ScoreboardInteractionInnerContent(
        incrementList,
        teamList,
        displayedScoreInfo,
        timeData,
        timerInProgress,
        viewModel::onEvent
    )
}

@Composable
private fun ScoreboardInteractionInnerContent(
    incrementList: List<List<Int>>,
    teamList: List<TeamDisplay>,
    displayedScoreInfo: DisplayedScoreInfo,
    timeData: TimeData,
    timerInProgress: Boolean,
    onEvent: (ScoreboardInteractionEvent) -> Unit
) {
    val currentTeamSize = incrementList.size
    if (currentTeamSize == 2) {
        Row {
            DefaultScoreContent(
                true,
                (displayedScoreInfo.displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display ?: "",
                incrementList[0],
                onEvent,
                modifier = Modifier.weight(1f)
            )
            val dividerColor = MaterialTheme.colorScheme.onBackground
            HorizontalDivider(
                color = dividerColor,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .width(40.dp)
                    .align(Alignment.CenterVertically),
                thickness = 20.dp
            )
            DefaultScoreContent(
                false,
                (displayedScoreInfo.displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display ?: "",
                incrementList[1],
                onEvent,
                modifier = Modifier.weight(1f)
            )
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            TimeDisplayContent(
                timeData,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
            TimeControlContent(
                timerInProgress,
                { onEvent(ScoreboardInteractionEvent.StartTimer) },
                { onEvent(ScoreboardInteractionEvent.PauseTimer(it)) },
                { onEvent(ScoreboardInteractionEvent.SkipTime(it)) },
                modifier = Modifier
                    .align(Alignment.TopStart)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
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

}

@PreviewScreenSizes
@Composable
private fun `2 Teams with long names`() =
    ScoreboardInteractionInnerContent(
        listOf(
            listOf(1, 2, 23),
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
    ) {}

@PreviewScreenSizes
@Composable
private fun `2 Teams with short names`() =
    ScoreboardInteractionInnerContent(
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
                DisplayedScore.CustomDisplayedScore("21"),
            ),
            DisplayedScore.Blank
        ),
        TimeData(12, 2, 4),
        false,
    ) {}
