package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.score

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.ui.common.SpecialScoreConstants
import com.dgnt.quickScoreboardCreator.ui.composable.AutoSizeText

@Composable
fun TwoScoreDisplay(
    modifier: Modifier = Modifier,
    displayedScoreInfo: DisplayedScoreInfo,

    ) {
    Row(
        modifier = modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (displayedScoreInfo.overallDisplayedScore != DisplayedScore.Blank)
            ScoreValueContent(
                modifier = Modifier.weight(1f),
                displayedScore = displayedScoreInfo.overallDisplayedScore,
            )
        else {
            ScoreValueContent(
                modifier = Modifier.weight(1f),
                displayedScore = displayedScoreInfo.displayedScores[0],
            )

            val dividerColor = MaterialTheme.colorScheme.onBackground
            HorizontalDivider(
                color = dividerColor,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .width(40.dp),
                thickness = 20.dp
            )

            ScoreValueContent(
                modifier = Modifier.weight(1f),
                displayedScore = displayedScoreInfo.displayedScores[1],
            )
        }

    }
}

@Composable
private fun ScoreValueContent(
    modifier: Modifier = Modifier,
    displayedScore: DisplayedScore,
) {
    val displayedValue = when (displayedScore) {
        DisplayedScore.Advantage -> stringResource(id = R.string.advantageDisplay)
        is DisplayedScore.CustomDisplayedScore -> displayedScore.display
        DisplayedScore.Deuce -> stringResource(id = R.string.deuceDisplay)
        DisplayedScore.Blank -> SpecialScoreConstants.NOTHING
    }
    AutoSizeText(
        text = displayedValue,
        maxTextSize = 200.sp,
        maxLines = 1,
        alignment = Alignment.Center,
        modifier = modifier
    )


}

@PreviewScreenSizes
@Composable
private fun `Normal scores`() =
    TwoScoreDisplay(
        displayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.CustomDisplayedScore("10"),
                DisplayedScore.CustomDisplayedScore("10"),
            ),
            DisplayedScore.Blank
        )
    )

@PreviewScreenSizes
@Composable
private fun `Deuce`() =
    TwoScoreDisplay(
        displayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Blank,
                DisplayedScore.Blank,
            ),
            DisplayedScore.Deuce
        )
    )