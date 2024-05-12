package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData

@Composable
fun TimerDisplayContent(
    modifier: Modifier = Modifier,
    timeData: TimeData,
) {
    Text(
        text = timeData.formatTime(),
        fontSize = 50.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

private fun TimeData.formatTime(): String {
    return if (minute == 0L)
        String.format("%d.%d", second, centiSecond)
    else
        String.format("%02d:%02d", minute, second)
}

@Preview(showBackground = true)
@Composable
private fun `12 seconds 500 milliseconds`() =
    TimerDisplayContent(timeData = TimeData(0, 12, 5))

@Preview(showBackground = true)
@Composable
private fun `2 seconds 500 milliseconds`() =
    TimerDisplayContent(timeData = TimeData(0, 2, 5))

@Preview(showBackground = true)
@Composable
private fun `12 minutes 8 seconds`() =
    TimerDisplayContent(timeData = TimeData(12, 8, 0))

@Preview(showBackground = true)
@Composable
private fun `13 minutes 35 seconds`() =
    TimerDisplayContent(timeData = TimeData(13, 35, 0))

@Preview(showBackground = true)
@Composable
private fun `13 minutes 35 seconds 800 milliseconds`() =
    TimerDisplayContent(timeData = TimeData(13, 35, 8))

@Preview(showBackground = true)
@Composable
private fun `213 minutes 35 seconds 800 milliseconds`() =
    TimerDisplayContent(timeData = TimeData(213, 35, 8))
