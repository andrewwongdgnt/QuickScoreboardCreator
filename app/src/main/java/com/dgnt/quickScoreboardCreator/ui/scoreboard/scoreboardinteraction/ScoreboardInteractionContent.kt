package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo

@Composable
fun ScoreboardInteractionContent(
    viewModel: ScoreboardInteractionViewModel = hiltViewModel()
) {

    val incrementList = viewModel.incrementList
    val displayedScoreInfo = viewModel.displayedScoreInfo
    ScoreboardInteractionInnerContent(
        incrementList,
        displayedScoreInfo,
        viewModel::onEvent
    )
}

@Composable
private fun ScoreboardInteractionInnerContent(
    incrementList: List<List<Int>>,
    displayedScoreInfo: DisplayedScoreInfo,
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
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 6.dp),
                thickness = 5.dp
            )
            DefaultScoreContent(
                false,
                (displayedScoreInfo.displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display ?: "",
                incrementList[1],
                onEvent,
                modifier = Modifier.weight(1f)
            )
        }
    }

}

@PreviewScreenSizes
@Composable
private fun `2 Teams`() =
    ScoreboardInteractionInnerContent(
        listOf(
            listOf(1, 2, 3),
            listOf(1, 2, 3),
        ),
        DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("10"),
                DisplayedScore.CustomDisplayedScore("21"),
            ),
            DisplayedScore.Blank
        )
    ) {}
