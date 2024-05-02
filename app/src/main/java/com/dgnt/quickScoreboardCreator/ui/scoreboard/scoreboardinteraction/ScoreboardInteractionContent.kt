package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.score.DefaultScoreContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time.TimeControlContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time.TimeDisplayContent

@Composable
fun ScoreboardInteractionContent(
    viewModel: ScoreboardInteractionViewModel = hiltViewModel()
) {

    val timeData = viewModel.timeData
    val timerInProgress = viewModel.timerInProgress
    val incrementList = viewModel.incrementList
    val displayedScoreInfo = viewModel.displayedScoreInfo
    ScoreboardInteractionInnerContent(
        incrementList,
        displayedScoreInfo,
        timeData,
        timerInProgress,
        viewModel::onEvent
    )
}

@Composable
private fun ScoreboardInteractionInnerContent(
    incrementList: List<List<Int>>,
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
            HorizontalDivider(
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
        Box(modifier = Modifier.fillMaxSize()) {
            TimeDisplayContent(
                timeData,
                Modifier.align(Alignment.TopCenter)
            )
            TimeControlContent(
                timerInProgress,
                { onEvent(ScoreboardInteractionEvent.StartTimer) },
                { onEvent(ScoreboardInteractionEvent.PauseTimer(it)) },
                { onEvent(ScoreboardInteractionEvent.SkipTime(it)) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

    }

}

@PreviewScreenSizes
@Composable
private fun `2 Teams`() =
    ScoreboardInteractionInnerContent(
        listOf(
            listOf(1, 2, 23),
            listOf(1, 2, 3),
        ),
        DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("10"),
                DisplayedScore.CustomDisplayedScore("21"),
            ),
            DisplayedScore.Blank
        ),
        TimeData(12,2,4),
        false,
    ) {}
