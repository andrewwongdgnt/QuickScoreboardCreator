package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.ui.common.composable.ScoringButton

@Composable
fun ScoreControl(
    modifier: Modifier = Modifier,
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
                number = i,
                index = index,
                onIncrement = onIncrement
            )
        }
    }

}


@PreviewScreenSizes
@Composable
private fun `left orientation`() =
    ScoreControl(
        incrementList = listOf(1, 12, 3),
        onIncrement = { _, _ -> }
    )