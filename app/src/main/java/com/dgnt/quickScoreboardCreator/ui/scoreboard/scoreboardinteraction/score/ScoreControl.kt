package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.ScoringButton
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.util.PreviewLandscape

@Composable
fun ScoreControl(
    modifier: Modifier = Modifier,
    simpleMode: Boolean,
    incrementList: List<Int>,
    onIncrement: (Int, Boolean) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        incrementList.forEachIndexed { index, i ->
            ScoringButton(
                buttonModifier = Modifier.padding(vertical = 10.dp),
                simpleMode = simpleMode,
                number = i,
                index = index,
                onIncrement = onIncrement
            )
        }
    }

}


@PreviewLandscape
@Composable
private fun `advance left orientation`() =
    ScoreControl(
        simpleMode = false,
        incrementList = listOf(1, 12, 3),
        onIncrement = { _, _ -> }
    )

@PreviewLandscape
@Composable
private fun `simple left orientation`() =
    ScoreControl(
        simpleMode = true,
        incrementList = listOf(1, 12, 3),
        onIncrement = { _, _ -> }
    )