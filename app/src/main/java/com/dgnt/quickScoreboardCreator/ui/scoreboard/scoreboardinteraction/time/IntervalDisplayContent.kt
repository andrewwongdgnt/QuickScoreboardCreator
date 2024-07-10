package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.Label
import com.dgnt.quickScoreboardCreator.ui.common.composable.value

@Composable
fun IntervalDisplayContent(
    modifier: Modifier = Modifier,
    intervalLabel: Label,
    currentInterval: Int,
) {
    Text(
        text = stringResource(id = R.string.intervalLabel, intervalLabel.value(), currentInterval),
        fontSize = 30.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
private fun `Default 1`() =
    IntervalDisplayContent(intervalLabel = Label.ResourceLabel(R.string.game), currentInterval = 1)

@Preview(showBackground = true)
@Composable
private fun `Custom 2`() =
    IntervalDisplayContent(intervalLabel = Label.CustomLabel("Custom"), currentInterval = 2)
