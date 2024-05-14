package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.R

@Composable
fun IntervalDisplayContent(
    modifier: Modifier = Modifier,
    labelInfo: Pair<String?, Int?>,
    currentInterval: Int,
) {
    Text(
        text = stringResource(id = R.string.intervalLabel, labelInfo.format(), currentInterval),
        fontSize = 30.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
    )

}

@Composable
private fun Pair<String?, Int?>.format(): String {
    return first ?: second?.let {
        stringResource(id = it)
    } ?: ""
}

@Preview(showBackground = true)
@Composable
private fun `Default 1`() =
    IntervalDisplayContent(labelInfo = Pair(null, R.string.game), currentInterval = 1)

@Preview(showBackground = true)
@Composable
private fun `Custom 2`() =
    IntervalDisplayContent(labelInfo = Pair("Custom", null), currentInterval = 2)
