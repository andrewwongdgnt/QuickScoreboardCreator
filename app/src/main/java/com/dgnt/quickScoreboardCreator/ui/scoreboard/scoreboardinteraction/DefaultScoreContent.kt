package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.ui.common.asIncrementDisplay
import com.dgnt.quickScoreboardCreator.ui.composable.AutoSizeText

@Composable
fun DefaultScoreContent(
    isOrientationLeft: Boolean,
    displayedScore: String,
    incrementList: List<Int>,
    onEvent: (ScoreboardInteractionEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        if (isOrientationLeft)
            ScoreIncrementContent(
                incrementList
            ) {
                onEvent(ScoreboardInteractionEvent.UpdateScore(0, it))
            }

        ScoreValueContent(
            displayedScore,
            modifier = Modifier.weight(1f)
        )

        if (!isOrientationLeft)
            ScoreIncrementContent(
                incrementList
            ) {
                onEvent(ScoreboardInteractionEvent.UpdateScore(1, it))
            }

    }
}

@Composable
private fun ScoreIncrementContent(
    incrementList: List<Int>,
    onIncrement: (Int) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.weight(1f))
        Column {
            incrementList.forEachIndexed { index, i ->
                Button(
                    onClick = {
                        onIncrement(index)
                    }
                ) {
                    Text(text = i.asIncrementDisplay())
                }
            }
        }
    }
}

@Composable
private fun ScoreValueContent(
    displayedScore: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        AutoSizeText(
            text = displayedScore,
            maxTextSize = 200.sp
        )
    }

}

@PreviewScreenSizes
@Composable
private fun `left orientation`() =
    DefaultScoreContent(
        true,
        "9",
        listOf(1, 2, 3),
        { }
    )

@PreviewScreenSizes
@Composable
private fun `right orientation`() =
    DefaultScoreContent(
        false,
        "9",
        listOf(1, 2, 3),
        { }
    )