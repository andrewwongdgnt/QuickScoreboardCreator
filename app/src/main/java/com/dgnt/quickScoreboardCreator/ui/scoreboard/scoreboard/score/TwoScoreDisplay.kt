package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboard.score

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
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.AutoSizeText
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.util.PreviewLandscape
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.value
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.core.presentation.ui.SpecialScoreConstants

@Composable
fun TwoScoreDisplay(
    modifier: Modifier = Modifier,
    simpleMode: Boolean,
    primaryDisplayedScoreInfo: com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo,
    secondaryDisplayedScoreInfo: com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo,
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
            if (primaryDisplayedScoreInfo.overallDisplayedScore != com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank)
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
            if (secondaryDisplayedScoreInfo.overallDisplayedScore != com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank)
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
                    onIncrement = { index, main ->
                        onScoreChange(false, 0, index, main)
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
                    onIncrement = { index, main ->
                        onScoreChange(false, 1, index, main)
                    }
                )
            }
        }
    }
}



@Composable
private fun ScoreValueContent(
    modifier: Modifier = Modifier,
    displayedScore: com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore,
    maxFontSize: TextUnit
) {
    val displayedValue = when (displayedScore) {
        com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Advantage -> stringResource(id = R.string.advantageDisplay)
        is com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom -> displayedScore.display
        com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Deuce -> stringResource(id = R.string.deuceDisplay)
        com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank -> SpecialScoreConstants.NOTHING
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
        primaryDisplayedScoreInfo = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
            listOf(
                com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("10"),
                com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("10"),
            ),
            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
        ),
        secondaryDisplayedScoreInfo = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
            listOf(
                com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
            ),
            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
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
        primaryDisplayedScoreInfo = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
            listOf(
                com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank,
                com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank,
            ),
            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Deuce
        ),
        secondaryDisplayedScoreInfo = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
            listOf(),
            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
        ),
        secondaryIncrementList = listOf(),
        secondaryScoreLabel = Label.Resource(R.string.blank),
        onScoreChange = { _, _, _, _ -> }
    )