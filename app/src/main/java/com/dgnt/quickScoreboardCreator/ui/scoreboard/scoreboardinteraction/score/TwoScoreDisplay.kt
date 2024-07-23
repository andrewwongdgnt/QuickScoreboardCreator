package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.ui.common.PreviewLandscape
import com.dgnt.quickScoreboardCreator.ui.common.SpecialScoreConstants
import com.dgnt.quickScoreboardCreator.ui.common.composable.AutoSizeText
import com.dgnt.quickScoreboardCreator.ui.common.composable.Label
import com.dgnt.quickScoreboardCreator.ui.common.composable.value

@Composable
fun TwoScoreDisplay(
    modifier: Modifier = Modifier,
    simpleMode: Boolean,
    primaryDisplayedScoreInfo: DisplayedScoreInfo,
    secondaryDisplayedScoreInfo: DisplayedScoreInfo,
    secondaryIncrementList: List<List<Int>>,
    secondaryScoreLabel: Label,
    onScoreChange: (Boolean, Int, Int, Boolean) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        val hasSecondaryScores = secondaryDisplayedScoreInfo.displayedScores.isNotEmpty()
        Row(

            modifier = Modifier.offset(y = if (hasSecondaryScores) 10.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            val maxFontSize = if (hasSecondaryScores) 135.sp else 200.sp
            if (primaryDisplayedScoreInfo.overallDisplayedScore != DisplayedScore.Blank)
                ScoreValueContent(
                    modifier = Modifier.weight(1f),
                    displayedScore = primaryDisplayedScoreInfo.overallDisplayedScore,
                    maxFontSize = maxFontSize
                )
            else {
                ScoreValueContent(
                    modifier = Modifier.weight(1f),
                    displayedScore = primaryDisplayedScoreInfo.displayedScores[0],
                    maxFontSize = maxFontSize
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
                    displayedScore = primaryDisplayedScoreInfo.displayedScores[1],
                    maxFontSize = maxFontSize
                )
            }
        }
        if (!hasSecondaryScores)
            return@Column
        Row(
            modifier = Modifier.offset(y = (-15).dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val maxFontSize = 50.sp
            if (secondaryDisplayedScoreInfo.overallDisplayedScore != DisplayedScore.Blank)
                ScoreValueContent(
                    modifier = Modifier.weight(1f),
                    displayedScore = secondaryDisplayedScoreInfo.overallDisplayedScore,
                    maxFontSize = maxFontSize
                )
            else {
                ScoreControl(
                    modifier = Modifier.weight(2f),
                    simpleMode = simpleMode,
                    incrementList = secondaryIncrementList[0],
                    onIncrement = { index, positive ->
                        onScoreChange(false, 0, index, positive)
                    }
                )
                ScoreValueContent(
                    modifier = Modifier.weight(1f),
                    displayedScore = secondaryDisplayedScoreInfo.displayedScores[0],
                    maxFontSize = maxFontSize
                )
                AutoSizeText(
                    text = secondaryScoreLabel.value(),
                    maxTextSize = maxFontSize,
                    maxLines = 1,
                    alignment = Alignment.Center,
                    modifier = modifier
                )
                ScoreValueContent(
                    modifier = Modifier.weight(1f),
                    displayedScore = secondaryDisplayedScoreInfo.displayedScores[1],
                    maxFontSize = maxFontSize
                )
                ScoreControl(
                    modifier = Modifier.weight(2f),
                    simpleMode = simpleMode,
                    incrementList = secondaryIncrementList[0],
                    onIncrement = { index, positive ->
                        onScoreChange(false, 1, index, positive)
                    }
                )
            }
        }
    }
}



@Composable
private fun ScoreValueContent(
    modifier: Modifier = Modifier,
    displayedScore: DisplayedScore,
    maxFontSize: TextUnit
) {
    val displayedValue = when (displayedScore) {
        DisplayedScore.Advantage -> stringResource(id = R.string.advantageDisplay)
        is DisplayedScore.Custom -> displayedScore.display
        DisplayedScore.Deuce -> stringResource(id = R.string.deuceDisplay)
        DisplayedScore.Blank -> SpecialScoreConstants.NOTHING
    }
    AutoSizeText(
        text = displayedValue,
        maxTextSize = maxFontSize,
        maxLines = 1,
        alignment = Alignment.Center,
        modifier = modifier
    )


}

@PreviewLandscape
@Composable
private fun `Normal scores`() =
    TwoScoreDisplay(
        simpleMode = false,
        primaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Custom("10"),
                DisplayedScore.Custom("10"),
            ),
            DisplayedScore.Blank
        ),
        secondaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Custom("1"),
                DisplayedScore.Custom("1"),
            ),
            DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(
            listOf(1),
            listOf(1),
        ),
        secondaryScoreLabel = Label.Resource(R.string.fouls),
        onScoreChange = { _, _, _, _ -> }
    )

@PreviewLandscape
@Composable
private fun `Deuce`() =
    TwoScoreDisplay(
        simpleMode = true,
        primaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(
                DisplayedScore.Blank,
                DisplayedScore.Blank,
            ),
            DisplayedScore.Deuce
        ),
        secondaryDisplayedScoreInfo = DisplayedScoreInfo(
            listOf(),
            DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(),
        secondaryScoreLabel = Label.Resource(R.string.blank),
        onScoreChange = { _, _, _, _ -> }
    )